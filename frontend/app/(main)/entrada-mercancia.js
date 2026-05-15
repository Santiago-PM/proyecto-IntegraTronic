import React, { useMemo, useState } from "react";
import { View, Text, StyleSheet, ScrollView, Platform } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Card, PrimaryButton, OutlineButton, PageHeader } from "../../src/components/ui";
import { FormField, SelectField, OptionPicker } from "../../src/components/FormModal";
import { colors, radius, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { productosApi, movimientosApi, stocksApi } from "../../src/services/api";
import { showAlert } from "../../src/utils/alert";

export default function EntradaMercanciaScreen() {
  const [nAlbaran, setNAlbaran] = useState("");
  const [proveedor, setProveedor] = useState("");
  const [lines, setLines] = useState([]);
  const [pickerOpen, setPickerOpen] = useState(false);
  const [saving, setSaving] = useState(false);

  const { data: productos } = useFetch(() => productosApi.list(), []);
  const { data: stocks, reload: reloadStocks } = useFetch(() => stocksApi.list(), []);

  const stockByProduct = useMemo(() => {
    const map = {};
    (stocks || []).forEach((s) => { map[s.idProducto] = s; });
    return map;
  }, [stocks]);

  const productOptions = (productos || []).map((p) => ({
    label: `${p.sku} — ${p.nombre}`,
    value: p.idProducto,
    raw: p,
  }));

  const addLine = (opt) => {
    setLines((prev) => {
      if (prev.find((l) => l.idProducto === opt.value)) return prev;
      return [...prev, { idProducto: opt.value, sku: opt.raw.sku, nombre: opt.raw.nombre, cantidad: "1" }];
    });
    setPickerOpen(false);
  };

  const register = async () => {
    if (lines.length === 0) return showAlert("Productos", "Añade al menos un producto.");
    setSaving(true);
    const desc = [nAlbaran && `Albarán ${nAlbaran}`, proveedor && `Proveedor ${proveedor}`].filter(Boolean).join(" · ");
    try {
      for (const line of lines) {
        const qty = parseInt(line.cantidad, 10);
        if (!qty || qty < 1) continue;
        await movimientosApi.create({
          tipoMovimiento: "ENTRADA",
          cantidad: qty,
          fechaMovimiento: new Date().toISOString().slice(0, 19),
          descripcion: desc || "Entrada de mercancía",
          idProducto: line.idProducto,
        });
        const stock = stockByProduct[line.idProducto];
        if (stock?.idStock) {
          await stocksApi.incrementar(stock.idStock, qty);
        }
      }
      setLines([]);
      setNAlbaran("");
      setProveedor("");
      reloadStocks();
      showAlert("Listo", "Entrada registrada y stock actualizado.");
    } catch (e) {
      showAlert("Error", e?.message || "No se pudo registrar.");
    } finally {
      setSaving(false);
    }
  };

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader title="Entrada de Mercancía" />
      <View style={styles.row}>
        <Card style={styles.col}>
          <Text style={styles.cardTitle}>Datos de Entrada</Text>
          <FormField label="Nº Albarán" value={nAlbaran} onChangeText={setNAlbaran} />
          <FormField label="Proveedor" value={proveedor} onChangeText={setProveedor} />
        </Card>
        <Card style={[styles.col, styles.productsCard]}>
          <View style={styles.cardHead}>
            <Text style={styles.cardTitle}>Productos</Text>
            <OutlineButton title="Añadir" onPress={() => setPickerOpen(true)} icon={<MaterialCommunityIcons name="plus" size={18} color={colors.primary} />} style={{ paddingVertical: 8, paddingHorizontal: 12 }} />
          </View>
          {lines.length === 0 ? (
            <Text style={styles.emptyText}>No hay productos añadidos</Text>
          ) : (
            lines.map((l) => (
              <View key={l.idProducto} style={styles.line}>
                <Text style={styles.lineName}>{l.sku} — {l.nombre}</Text>
                <FormField label="Cantidad" value={l.cantidad} onChangeText={(v) => setLines((prev) => prev.map((x) => x.idProducto === l.idProducto ? { ...x, cantidad: v } : x))} keyboardType="number-pad" />
              </View>
            ))
          )}
          <PrimaryButton
            title={saving ? "Registrando..." : "Registrar Entrada"}
            disabled={saving || lines.length === 0}
            onPress={register}
            icon={<MaterialCommunityIcons name="content-save-outline" size={20} color="#fff" />}
            style={{ alignSelf: "flex-end", minWidth: 200, marginTop: spacing.lg }}
          />
        </Card>
      </View>
      <OptionPicker visible={pickerOpen} title="Producto" onClose={() => setPickerOpen(false)} options={productOptions} onSelect={addLine} />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  row: { flexDirection: "row", flexWrap: "wrap", gap: spacing.md },
  col: { flex: 1, minWidth: 280 },
  productsCard: { minHeight: 320 },
  cardTitle: { fontSize: 16, fontWeight: "700", color: colors.text, marginBottom: spacing.md },
  cardHead: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", marginBottom: spacing.sm },
  emptyText: { color: colors.textMuted, textAlign: "center", marginTop: spacing.lg },
  line: { marginBottom: spacing.md, paddingBottom: spacing.sm, borderBottomWidth: 1, borderBottomColor: colors.border },
  lineName: { fontWeight: "600", marginBottom: spacing.sm, color: colors.text },
});
