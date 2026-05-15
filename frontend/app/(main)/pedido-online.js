import React, { useMemo, useState } from "react";
import { View, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import {
  SearchField,
  TableShell,
  PaginationFooter,
  PageHeader,
  PrimaryButton,
  ActionLink,
} from "../../src/components/ui";
import { FormModal, SelectField, OptionPicker } from "../../src/components/FormModal";
import { spacing, colors } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { pedidosApi, clientesApi, productosApi, pagosApi } from "../../src/services/api";
import { formatDate, formatMoney } from "../../src/utils/format";
import { showAlert } from "../../src/utils/alert";

export default function PedidoOnlineScreen() {
  const [q, setQ] = useState("");
  const [modal, setModal] = useState(false);
  const [pickerCliente, setPickerCliente] = useState(false);
  const [pickerProducto, setPickerProducto] = useState(false);
  const [idCliente, setIdCliente] = useState(null);
  const [nombreCliente, setNombreCliente] = useState("");
  const [lineas, setLineas] = useState([]);
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const { data: pedidos, loading, error, reload } = useFetch(() => pedidosApi.list(), []);
  const { data: clientes } = useFetch(() => clientesApi.list(), []);
  const { data: productos } = useFetch(() => productosApi.list(), []);

  const filtered = useMemo(() => {
    const list = pedidos || [];
    const term = q.trim().toLowerCase();
    if (!term) return list;
    return list.filter(
      (p) =>
        String(p.idPedido).includes(term) ||
        p.nombreCliente?.toLowerCase().includes(term) ||
        p.estado?.toLowerCase().includes(term)
    );
  }, [pedidos, q]);

  const handleCreate = async () => {
    setFormError(null);
    if (!idCliente) return setFormError("Selecciona un cliente.");
    if (lineas.length === 0) return setFormError("Añade al menos un producto.");
    setSaving(true);
    try {
      await pedidosApi.registrar({
        idCliente,
        lineas: lineas.map((l) => ({ idProducto: l.idProducto, cantidad: l.cantidad })),
      });
      setModal(false);
      setLineas([]);
      setIdCliente(null);
      setNombreCliente("");
      reload();
      showAlert("Listo", "Pedido registrado.");
    } catch (e) {
      setFormError(e?.message || "Error al crear pedido.");
    } finally {
      setSaving(false);
    }
  };

  const registerPayment = async (pedido) => {
    try {
      await pagosApi.registrar({
        idPedido: pedido.idPedido,
        importe: Number(pedido.total),
        estadoPago: "PAGADO",
        metodoPago: "TARJETA",
      });
      reload();
      showAlert("Pago registrado", formatMoney(pedido.total));
    } catch (e) {
      showAlert("Error", e?.message || "No se pudo registrar el pago.");
    }
  };

  const rows = filtered.map((p) => ({
    key: p.idPedido,
    cells: [
      `#${p.idPedido}`,
      p.nombreCliente || "—",
      formatDate(p.fechaPedido),
      "—",
      formatMoney(p.total),
      p.estado || "—",
      p.estado !== "PAGADO" ? (
        <ActionLink key="pay" label="Pagar" color={colors.primary} onPress={() => registerPayment(p)} />
      ) : (
        "Pagado"
      ),
    ],
  }));

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Pedidos Online"
        right={
          <PrimaryButton
            title="Nuevo Pedido"
            onPress={() => {
              setModal(true);
              setFormError(null);
              setLineas([]);
            }}
            icon={<MaterialCommunityIcons name="plus" size={20} color="#fff" />}
          />
        }
      />
      <SearchField placeholder="Buscar pedido..." value={q} onChangeText={setQ} />
      <TableShell
        columns={["N° Pedido", "Cliente", "Fecha", "—", "Total", "Estado", "Acciones"]}
        rows={rows}
        loading={loading}
        error={error}
        emptyText="No hay pedidos"
        footer={<PaginationFooter total={rows.length} />}
      />

      <FormModal visible={modal} title="Nuevo pedido online" onClose={() => setModal(false)} onSubmit={handleCreate} loading={saving} error={formError}>
        <SelectField label="Cliente *" value={nombreCliente} placeholder="Seleccionar..." onPress={() => setPickerCliente(true)} />
        <PrimaryButton
          title="Añadir producto"
          onPress={() => setPickerProducto(true)}
          icon={<MaterialCommunityIcons name="plus" size={18} color="#fff" />}
          style={{ marginBottom: spacing.md }}
        />
        {lineas.map((l) => (
          <View key={l.idProducto} style={styles.lineRow}>
            <Text style={styles.lineTxt}>{l.nombre} × {l.cantidad}</Text>
            <ActionLink label="Quitar" onPress={() => setLineas((prev) => prev.filter((x) => x.idProducto !== l.idProducto))} />
          </View>
        ))}
      </FormModal>

      <OptionPicker
        visible={pickerCliente}
        title="Cliente"
        onClose={() => setPickerCliente(false)}
        options={(clientes || []).map((c) => ({ label: c.nombre, value: c.idCliente }))}
        onSelect={(opt) => {
          setIdCliente(opt.value);
          setNombreCliente(opt.label);
          setPickerCliente(false);
        }}
      />
      <OptionPicker
        visible={pickerProducto}
        title="Producto"
        onClose={() => setPickerProducto(false)}
        options={(productos || []).map((p) => ({ label: `${p.sku} — ${p.nombre}`, value: p.idProducto, raw: p }))}
        onSelect={(opt) => {
          setLineas((prev) => {
            const ex = prev.find((l) => l.idProducto === opt.value);
            if (ex) return prev.map((l) => (l.idProducto === opt.value ? { ...l, cantidad: l.cantidad + 1 } : l));
            return [...prev, { idProducto: opt.value, nombre: opt.raw.nombre, cantidad: 1 }];
          });
          setPickerProducto(false);
        }}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  lineRow: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", marginBottom: 8 },
  lineTxt: { color: colors.text, flex: 1 },
});
