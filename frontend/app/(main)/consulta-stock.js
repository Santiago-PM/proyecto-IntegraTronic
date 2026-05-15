import React, { useMemo, useState } from "react";
import { View, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import {
  SearchField,
  TableShell,
  PaginationFooter,
  PageHeader,
  StatMini,
  OutlineButton,
  ActionLink,
} from "../../src/components/ui";
import { FormModal, FormField } from "../../src/components/FormModal";
import { colors, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { stocksApi } from "../../src/services/api";
import { exportToCsv } from "../../src/utils/exportCsv";
import { showAlert } from "../../src/utils/alert";

function stockLevel(cantidad, minimo) {
  if (cantidad <= 0) return { label: "Crítico", color: colors.danger };
  if (cantidad <= minimo) return { label: "Bajo", color: colors.warning };
  return { label: "Óptimo", color: colors.success };
}

export default function ConsultaStockScreen() {
  const [q, setQ] = useState("");
  const [modal, setModal] = useState(false);
  const [stockId, setStockId] = useState(null);
  const [cantidad, setCantidad] = useState("1");
  const [saving, setSaving] = useState(false);

  const { data: stocks, loading, error, reload } = useFetch(() => stocksApi.list(), []);

  const stats = useMemo(() => {
    const list = stocks || [];
    let optimo = 0, bajo = 0, critico = 0;
    list.forEach((s) => {
      const n = stockLevel(s.cantidadDisponible, s.stockMinimo).label;
      if (n === "Óptimo") optimo += 1;
      else if (n === "Bajo") bajo += 1;
      else critico += 1;
    });
    return { total: list.length, optimo, bajo, critico };
  }, [stocks]);

  const filtered = useMemo(() => {
    const list = stocks || [];
    const term = q.trim().toLowerCase();
    if (!term) return list;
    return list.filter(
      (s) =>
        s.skuProducto?.toLowerCase().includes(term) ||
        s.nombreProducto?.toLowerCase().includes(term)
    );
  }, [stocks, q]);

  const handleExport = () => {
    const cols = ["SKU", "Producto", "Stock", "Min", "Nivel"];
    const data = filtered.map((s) => {
      const n = stockLevel(s.cantidadDisponible, s.stockMinimo);
      return [s.skuProducto, s.nombreProducto, s.cantidadDisponible, s.stockMinimo, n.label];
    });
    if (exportToCsv("stock", cols, data)) showAlert("Exportado", "stock.csv descargado.");
    else showAlert("Exportar", "Disponible en web.");
  };

  const rows = filtered.map((s) => {
    const nivel = stockLevel(s.cantidadDisponible, s.stockMinimo);
    return {
      key: s.idStock,
      cells: [
        s.skuProducto || "—",
        s.nombreProducto || "—",
        "—",
        String(s.cantidadDisponible ?? 0),
        `${s.stockMinimo ?? 0}`,
        nivel.label,
        "—",
        <ActionLink
          key="inc"
          label="+ Stock"
          color={colors.primary}
          onPress={() => {
            setStockId(s.idStock);
            setCantidad("1");
            setModal(true);
          }}
        />,
      ],
    };
  });

  const addStock = async () => {
    const qty = parseInt(cantidad, 10);
    if (!stockId || !qty || qty < 1) return showAlert("Cantidad", "Introduce una cantidad válida.");
    setSaving(true);
    try {
      await stocksApi.incrementar(stockId, qty);
      setModal(false);
      reload();
      showAlert("Listo", `+${qty} unidades añadidas.`);
    } catch (e) {
      showAlert("Error", e?.message);
    } finally {
      setSaving(false);
    }
  };

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Consulta y Control de Stock"
        subtitle="Monitoreo en tiempo real del inventario"
        right={
          <OutlineButton
            title="Exportar"
            onPress={handleExport}
            icon={<MaterialCommunityIcons name="download" size={18} color={colors.primary} />}
          />
        }
      />
      <View style={styles.stats}>
        <StatMini label="Total" value={String(stats.total)} iconBg={colors.chartBlue} icon={<MaterialCommunityIcons name="package-variant" size={20} color="#fff" />} />
        <StatMini label="Óptimo" value={String(stats.optimo)} valueColor={colors.success} iconBg={colors.success} icon={<MaterialCommunityIcons name="trending-up" size={20} color="#fff" />} />
        <StatMini label="Bajo" value={String(stats.bajo)} valueColor={colors.warning} iconBg={colors.warning} icon={<MaterialCommunityIcons name="trending-down" size={20} color="#fff" />} />
        <StatMini label="Crítico" value={String(stats.critico)} valueColor={colors.danger} iconBg={colors.danger} icon={<MaterialCommunityIcons name="alert" size={20} color="#fff" />} />
      </View>
      <SearchField placeholder="Buscar por SKU o nombre..." value={q} onChangeText={setQ} />
      <TableShell columns={["SKU", "Producto", "Familia", "Stock", "Mín", "Nivel", "—", "Acciones"]} rows={rows} loading={loading} error={error} emptyText="Sin datos" footer={<PaginationFooter total={rows.length} />} />

      <FormModal visible={modal} title="Añadir stock" onClose={() => setModal(false)} onSubmit={addStock} loading={saving} submitLabel="Añadir">
        <FormField label="Cantidad" value={cantidad} onChangeText={setCantidad} keyboardType="number-pad" />
      </FormModal>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  stats: { flexDirection: "row", flexWrap: "wrap", gap: spacing.md, marginBottom: spacing.lg },
});
