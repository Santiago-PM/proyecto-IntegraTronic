import React, { useMemo, useState } from "react";
import { View, Pressable, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import {
  PrimaryButton,
  OutlineButton,
  SearchField,
  TableShell,
  PaginationFooter,
  PageHeader,
  ActionLink,
} from "../../src/components/ui";
import { FormModal, FormField, SelectField, OptionPicker } from "../../src/components/FormModal";
import { colors, radius, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { productosApi, stocksApi, familiasApi } from "../../src/services/api";
import { formatMoney } from "../../src/utils/format";
import { exportToCsv } from "../../src/utils/exportCsv";
import { showAlert, confirmAction } from "../../src/utils/alert";

const EMPTY = { sku: "", nombre: "", descripcion: "", precio: "", idFamilia: null, nombreFamilia: "", stockInicial: "0", stockMinimo: "2" };

export default function ProductosScreen() {
  const [q, setQ] = useState("");
  const [modal, setModal] = useState(false);
  const [pickerFamilia, setPickerFamilia] = useState(false);
  const [form, setForm] = useState(EMPTY);
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const { data: productos, loading, error, reload } = useFetch(() => productosApi.list(), []);
  const { data: stocks, reload: reloadStocks } = useFetch(() => stocksApi.list(), []);
  const { data: familias } = useFetch(() => familiasApi.list(), []);

  const stockByProduct = useMemo(() => {
    const map = {};
    (stocks || []).forEach((s) => { map[s.idProducto] = s; });
    return map;
  }, [stocks]);

  const filtered = useMemo(() => {
    const list = productos || [];
    const term = q.trim().toLowerCase();
    if (!term) return list;
    return list.filter(
      (p) =>
        p.sku?.toLowerCase().includes(term) ||
        p.nombre?.toLowerCase().includes(term) ||
        p.nombreFamilia?.toLowerCase().includes(term)
    );
  }, [productos, q]);

  const handleExport = () => {
    const cols = ["SKU", "Nombre", "Familia", "Precio", "Stock", "Stock Min.", "Estado"];
    const data = filtered.map((p) => {
      const s = stockByProduct[p.idProducto];
      return [p.sku, p.nombre, p.nombreFamilia, p.precio, s?.cantidadDisponible ?? 0, s?.stockMinimo ?? 0, p.activo ? "Activo" : "Inactivo"];
    });
    if (exportToCsv("productos", cols, data)) showAlert("Exportado", "Archivo productos.csv descargado.");
    else showAlert("Exportar", "La exportación CSV está disponible en la versión web.");
  };

  const handleDelete = async (id) => {
    if (!(await confirmAction("Eliminar", "¿Eliminar este producto?"))) return;
    try {
      await productosApi.remove(id);
      reload();
      reloadStocks();
    } catch (e) {
      showAlert("Error", e?.message || "No se pudo eliminar.");
    }
  };

  const handleSave = async () => {
    setFormError(null);
    if (!form.sku.trim() || !form.nombre.trim()) return setFormError("SKU y nombre son obligatorios.");
    if (!form.idFamilia) return setFormError("Selecciona una familia.");
    const precio = parseFloat(String(form.precio).replace(",", "."));
    if (Number.isNaN(precio) || precio < 0) return setFormError("Precio no válido.");

    setSaving(true);
    try {
      const creado = await productosApi.create({
        sku: form.sku.trim(),
        nombre: form.nombre.trim(),
        descripcion: form.descripcion.trim() || null,
        precio,
        activo: true,
        idFamilia: form.idFamilia,
      });
      const qty = parseInt(form.stockInicial, 10) || 0;
      const min = parseInt(form.stockMinimo, 10) || 0;
      if (qty > 0 || min > 0) {
        await stocksApi.create({ idProducto: creado.idProducto, cantidadDisponible: qty, stockMinimo: min });
      }
      setModal(false);
      reload();
      reloadStocks();
      showAlert("Listo", "Producto creado.");
    } catch (e) {
      setFormError(e?.message || "Error al guardar.");
    } finally {
      setSaving(false);
    }
  };

  const rows = filtered.map((p) => {
    const stock = stockByProduct[p.idProducto];
    return {
      key: p.idProducto,
      cells: [
        p.sku || "—",
        p.nombre || "—",
        p.nombreFamilia || "—",
        formatMoney(p.precio),
        String(stock?.cantidadDisponible ?? 0),
        String(stock?.stockMinimo ?? 0),
        p.activo ? "Activo" : "Inactivo",
        <ActionLink key="del" onPress={() => handleDelete(p.idProducto)} />,
      ],
    };
  });

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Gestión de Productos"
        subtitle="Administra el catálogo completo de productos"
        right={
          <View style={styles.actions}>
            <OutlineButton title="Exportar" onPress={handleExport} icon={<MaterialCommunityIcons name="download" size={18} color={colors.primary} />} style={{ paddingVertical: 8 }} />
            <PrimaryButton title="Nuevo Producto" onPress={() => { setForm(EMPTY); setFormError(null); setModal(true); }} icon={<MaterialCommunityIcons name="plus" size={20} color="#fff" />} style={{ paddingVertical: 10 }} />
          </View>
        }
      />
      <View style={styles.toolbar}>
        <SearchField placeholder="Buscar por SKU, nombre..." value={q} onChangeText={setQ} />
      </View>
      <TableShell columns={["SKU", "Nombre", "Familia", "Precio", "Stock", "Stock Min.", "Estado", "Acciones"]} rows={rows} loading={loading} error={error} emptyText="No hay productos registrados" footer={<PaginationFooter total={rows.length} />} />

      <FormModal visible={modal} title="Nuevo producto" onClose={() => setModal(false)} onSubmit={handleSave} loading={saving} error={formError}>
        <FormField label="SKU *" value={form.sku} onChangeText={(v) => setForm((f) => ({ ...f, sku: v }))} placeholder="PORT-002" />
        <FormField label="Nombre *" value={form.nombre} onChangeText={(v) => setForm((f) => ({ ...f, nombre: v }))} />
        <FormField label="Descripción" value={form.descripcion} onChangeText={(v) => setForm((f) => ({ ...f, descripcion: v }))} multiline />
        <FormField label="Precio (€) *" value={form.precio} onChangeText={(v) => setForm((f) => ({ ...f, precio: v }))} keyboardType="decimal-pad" />
        <SelectField label="Familia *" value={form.nombreFamilia} placeholder="Seleccionar..." onPress={() => setPickerFamilia(true)} />
        <View style={styles.row2}>
          <View style={{ flex: 1 }}><FormField label="Stock inicial" value={form.stockInicial} onChangeText={(v) => setForm((f) => ({ ...f, stockInicial: v }))} keyboardType="number-pad" /></View>
          <View style={{ flex: 1 }}><FormField label="Stock mínimo" value={form.stockMinimo} onChangeText={(v) => setForm((f) => ({ ...f, stockMinimo: v }))} keyboardType="number-pad" /></View>
        </View>
      </FormModal>

      <OptionPicker
        visible={pickerFamilia}
        title="Familia"
        onClose={() => setPickerFamilia(false)}
        options={(familias || []).map((f) => ({ label: f.nombre, value: f.idFamilia, raw: f }))}
        onSelect={(opt) => {
          setForm((f) => ({ ...f, idFamilia: opt.value, nombreFamilia: opt.label }));
          setPickerFamilia(false);
        }}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  actions: { flexDirection: "row", gap: spacing.sm, flexWrap: "wrap", justifyContent: "flex-end" },
  toolbar: { flexDirection: "row", alignItems: "center", gap: spacing.sm, marginBottom: spacing.sm },
  row2: { flexDirection: "row", gap: spacing.md },
});
