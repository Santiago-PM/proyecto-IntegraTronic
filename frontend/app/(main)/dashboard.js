import React, { useMemo } from "react";
import { View, Text, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Card, PageHeader, Badge, MetricCard } from "../../src/components/ui";
import { colors, radius, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { clientesApi, informesApi, productosApi, stocksApi } from "../../src/services/api";
import { formatMoney } from "../../src/utils/format";

export default function DashboardScreen() {
  const { data: productos } = useFetch(() => productosApi.list(), []);
  const { data: clientes } = useFetch(() => clientesApi.list(), []);
  const { data: informe } = useFetch(() => informesApi.ventas(), []);
  const { data: stocks } = useFetch(() => stocksApi.list(), []);

  const stockBajo = useMemo(() => {
    if (!stocks) return 0;
    return stocks.filter((s) => s.cantidadDisponible <= s.stockMinimo).length;
  }, [stocks]);

  const ventasHoy = informe?.totalVentasFisicas ?? 0;
  const ingresosMes =
    Number(informe?.importeTotalVentasFisicas || 0) +
    Number(informe?.importeTotalPedidosOnline || 0);

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Dashboard Principal"
        subtitle="Resumen general del sistema"
        right={<Badge text="Conectado al backend" />}
      />

      <View style={styles.row4}>
        <MetricCard
          title="Total Productos"
          value={String(productos?.length ?? 0)}
          sub="Datos en vivo"
          iconBg={colors.chartBlue}
          icon={<MaterialCommunityIcons name="package-variant" size={22} color={colors.white} />}
        />
        <MetricCard
          title="Clientes Activos"
          value={String(clientes?.length ?? 0)}
          sub="Registrados"
          iconBg={colors.success}
          icon={<MaterialCommunityIcons name="account-group" size={22} color={colors.white} />}
        />
        <MetricCard
          title="Ventas Físicas"
          value={String(ventasHoy)}
          sub={`Pedidos online: ${informe?.totalPedidosOnline ?? 0}`}
          subColor={colors.textMuted}
          iconBg={colors.purple}
          icon={<MaterialCommunityIcons name="cart" size={22} color={colors.white} />}
        />
        <MetricCard
          title="Ingresos Totales"
          value={formatMoney(ingresosMes)}
          sub="Ventas + pedidos"
          iconBg={colors.orange}
          icon={<MaterialCommunityIcons name="chart-line" size={22} color={colors.white} />}
        />
      </View>

      <View style={styles.row2}>
        <Card style={styles.chartCard}>
          <Text style={styles.chartTitle}>Resumen de operaciones</Text>
          <View style={styles.summaryList}>
            <SummaryRow label="Ventas físicas" value={String(informe?.totalVentasFisicas ?? 0)} />
            <SummaryRow label="Pedidos online" value={String(informe?.totalPedidosOnline ?? 0)} />
            <SummaryRow label="Pagos online" value={String(informe?.totalPagosOnline ?? 0)} />
            <SummaryRow label="Importe ventas" value={formatMoney(informe?.importeTotalVentasFisicas)} />
            <SummaryRow label="Importe pedidos" value={formatMoney(informe?.importeTotalPedidosOnline)} />
          </View>
        </Card>
        <Card style={styles.chartCard}>
          <Text style={styles.chartTitle}>Alertas de Stock</Text>
          <View style={styles.alertBox}>
            <MaterialCommunityIcons
              name={stockBajo > 0 ? "alert" : "check-circle"}
              size={22}
              color={stockBajo > 0 ? colors.warning : colors.success}
              style={{ marginRight: 8 }}
            />
            <Text style={styles.alertOk}>
              {stockBajo > 0
                ? `${stockBajo} producto(s) con stock bajo o crítico`
                : "No hay productos con stock bajo"}
            </Text>
          </View>
        </Card>
      </View>

      <Card>
        <Text style={styles.sectionTitle}>Actividad Reciente</Text>
        <View style={styles.activityRow}>
          <View style={styles.dot} />
          <View style={{ flex: 1 }}>
            <Text style={styles.activityText}>Dashboard sincronizado con la API REST</Text>
            <Text style={styles.time}>IntegraTronic backend · puerto 8080</Text>
          </View>
        </View>
      </Card>
    </ScrollView>
  );
}

function SummaryRow({ label, value }) {
  return (
    <View style={styles.summaryRow}>
      <Text style={styles.summaryLabel}>{label}</Text>
      <Text style={styles.summaryValue}>{value}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  scroll: {
    paddingBottom: spacing.xl,
  },
  row4: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: spacing.md,
    marginBottom: spacing.lg,
  },
  row2: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: spacing.md,
    marginBottom: spacing.lg,
  },
  chartCard: {
    flex: 1,
    minWidth: 260,
    minHeight: 220,
  },
  chartTitle: {
    fontSize: 16,
    fontWeight: "700",
    color: colors.text,
    marginBottom: spacing.md,
  },
  summaryList: {
    gap: spacing.sm,
  },
  summaryRow: {
    flexDirection: "row",
    justifyContent: "space-between",
    paddingVertical: 6,
    borderBottomWidth: 1,
    borderBottomColor: colors.border,
  },
  summaryLabel: {
    fontSize: 14,
    color: colors.textMuted,
  },
  summaryValue: {
    fontSize: 14,
    fontWeight: "700",
    color: colors.text,
  },
  sectionTitle: {
    fontSize: 16,
    fontWeight: "700",
    marginBottom: spacing.md,
    color: colors.text,
  },
  activityRow: {
    flexDirection: "row",
    alignItems: "flex-start",
  },
  dot: {
    width: 10,
    height: 10,
    borderRadius: 5,
    backgroundColor: colors.primary,
    marginTop: 5,
    marginRight: 10,
  },
  activityText: {
    fontSize: 15,
    color: colors.text,
  },
  time: {
    marginTop: 4,
    fontSize: 13,
    color: colors.textLight,
  },
  alertBox: {
    flexDirection: "row",
    alignItems: "center",
    backgroundColor: "#ECFDF5",
    padding: spacing.md,
    borderRadius: radius.md,
    marginTop: spacing.sm,
  },
  alertOk: {
    flex: 1,
    color: colors.text,
    fontSize: 14,
  },
});
