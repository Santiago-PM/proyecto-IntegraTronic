import React, { useMemo, useState } from "react";
import { View, Text, Pressable, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { useFetch } from "../../src/hooks/useFetch";
import { informesApi, productosApi, clientesApi, stocksApi } from "../../src/services/api";
import { formatMoney } from "../../src/utils/format";
import { exportToCsv } from "../../src/utils/exportCsv";
import { showAlert } from "../../src/utils/alert";
import { Card, PageHeader, PrimaryButton } from "../../src/components/ui";
import { colors, radius, spacing } from "../../src/constants/theme";

const TABS = ["RESUMEN DE VENTAS", "MÉTRICAS GENERALES"];

export default function InformesScreen() {
  const [tab, setTab] = useState(0);
  const { data: informe, loading, error, reload } = useFetch(() => informesApi.ventas(), []);
  const { data: productos } = useFetch(() => productosApi.list(), []);
  const { data: clientes } = useFetch(() => clientesApi.list(), []);
  const { data: stocks } = useFetch(() => stocksApi.list(), []);

  const productosBajoStock = useMemo(
    () => (stocks || []).filter((s) => s.cantidadDisponible <= s.stockMinimo).length,
    [stocks]
  );

  const ingresosTotales = useMemo(() => {
    if (!informe) return 0;
    return Number(informe.importeTotalVentasFisicas || 0) + Number(informe.importeTotalPedidosOnline || 0);
  }, [informe]);

  const handleExport = () => {
    if (!informe) return showAlert("Informe", "No hay datos para exportar.");
    const cols = ["Concepto", "Cantidad", "Importe"];
    const data = [
      ["Ventas fisicas", informe.totalVentasFisicas, informe.importeTotalVentasFisicas],
      ["Pedidos online", informe.totalPedidosOnline, informe.importeTotalPedidosOnline],
      ["Pagos online", informe.totalPagosOnline, informe.importeTotalPagosOnline],
    ];
    if (exportToCsv("informe-ventas", cols, data)) showAlert("Exportado", "informe-ventas.csv descargado.");
    else showAlert("Exportar", "Disponible en la version web.");
  };

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader title="Informes de ventas" />

      <View style={styles.tabs}>
        {TABS.map((t, i) => (
          <Pressable key={t} onPress={() => setTab(i)} style={styles.tabHit}>
            <Text style={[styles.tabTxt, i === tab && styles.tabActive]}>{t}</Text>
            {i === tab ? <View style={styles.und} /> : <View style={{ height: 3 }} />}
          </Pressable>
        ))}
      </View>

      {tab === 0 ? (
        <Card style={styles.card}>
          <Text style={styles.sectionTitle}>Resumen de ventas</Text>
          {error ? (
            <View style={styles.note}>
              <Text style={[styles.noteTxt, { color: colors.danger }]}>{error}</Text>
            </View>
          ) : (
            <>
              <View style={styles.grid}>
                <Metric label="Ventas físicas" value={String(informe?.totalVentasFisicas ?? 0)} />
                <Metric label="Pedidos online" value={String(informe?.totalPedidosOnline ?? 0)} />
                <Metric label="Pagos online" value={String(informe?.totalPagosOnline ?? 0)} />
                <Metric
                  label="Importe de ventas físicas"
                  value={formatMoney(informe?.importeTotalVentasFisicas || 0)}
                />
                <Metric
                  label="Importe de pedidos online"
                  value={formatMoney(informe?.importeTotalPedidosOnline || 0)}
                />
                <Metric
                  label="Importe de pagos online"
                  value={formatMoney(informe?.importeTotalPagosOnline || 0)}
                />
              </View>

              <View style={styles.actions}>
                <PrimaryButton
                  title={loading ? "Cargando..." : "Actualizar informe"}
                  disabled={loading}
                  onPress={reload}
                  icon={<MaterialCommunityIcons name="refresh" size={20} color="#fff" />}
                  style={styles.actionBtn}
                />
                <PrimaryButton
                  title="Exportar CSV"
                  disabled={!informe}
                  onPress={handleExport}
                  icon={<MaterialCommunityIcons name="download" size={20} color="#fff" />}
                  style={styles.actionBtn}
                />
              </View>
            </>
          )}
        </Card>
      ) : (
        <Card style={styles.card}>
          <Text style={styles.sectionTitle}>Métricas generales</Text>
          <View style={styles.grid}>
            <Metric label="Productos registrados" value={String(productos?.length ?? 0)} />
            <Metric label="Clientes registrados" value={String(clientes?.length ?? 0)} />
            <Metric label="Productos bajo stock mínimo" value={String(productosBajoStock)} />
            <Metric label="Ingresos totales" value={formatMoney(ingresosTotales)} />
          </View>
          <View style={styles.note}>
            <Text style={styles.noteTxt}>
              Datos calculados con los endpoints actuales del backend.
            </Text>
          </View>
        </Card>
      )}
    </ScrollView>
  );
}

function Metric({ label, value }) {
  return (
    <View style={styles.metric}>
      <Text style={styles.metricLabel}>{label}</Text>
      <Text style={styles.metricValue}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl * 2 },
  tabs: { flexDirection: "row", flexWrap: "wrap", gap: spacing.lg, marginBottom: spacing.lg },
  tabHit: { marginRight: spacing.md },
  tabTxt: { fontSize: 12, fontWeight: "800", color: colors.textMuted },
  tabActive: { color: colors.primary },
  und: { marginTop: 8, height: 3, backgroundColor: colors.primary, borderRadius: 2 },
  card: { marginBottom: spacing.lg },
  sectionTitle: { fontSize: 16, fontWeight: "700", color: colors.text, marginBottom: spacing.md },
  grid: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: spacing.md,
  },
  metric: {
    flex: 1,
    minWidth: 210,
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: radius.md,
    padding: spacing.md,
    backgroundColor: colors.white,
  },
  metricLabel: { color: colors.textMuted, fontSize: 13, marginBottom: 6 },
  metricValue: { color: colors.text, fontSize: 20, fontWeight: "800" },
  actions: { flexDirection: "row", gap: spacing.sm, marginTop: spacing.lg, flexWrap: "wrap" },
  actionBtn: { flex: 1, minWidth: 150 },
  note: {
    marginTop: spacing.md,
    padding: spacing.md,
    backgroundColor: colors.activeNavBg,
    borderRadius: radius.md,
  },
  noteTxt: { fontSize: 13, color: colors.primary, fontWeight: "600" },
});
