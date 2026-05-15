import React, { useState } from "react";
import { useFetch } from "../../src/hooks/useFetch";
import { informesApi, productosApi, clientesApi, stocksApi } from "../../src/services/api";
import { formatMoney } from "../../src/utils/format";
import { exportToCsv } from "../../src/utils/exportCsv";
import { showAlert } from "../../src/utils/alert";
import {
  View,
  Text,
  Pressable,
  StyleSheet,
  ScrollView,
  Modal,
  TouchableWithoutFeedback,
  Platform,
} from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Card, PageHeader, PrimaryButton } from "../../src/components/ui";
import { colors, radius, spacing } from "../../src/constants/theme";

const TABS = ["GENERADOR DE INFORMES", "ANÁLISIS EN TIEMPO REAL", "INFORMES PROGRAMADOS"];

const TIPOS = ["Seleccionar...", "Informe de Ventas", "Informe de Inventario", "Análisis de Productos", "Análisis de Clientes"];
const PERIODOS = [
  "Seleccionar...",
  "Hoy",
  "Ayer",
  "Esta Semana",
  "Este Mes",
  "Este Trimestre",
  "Este Año",
  "Personalizado",
];

const REPORT_CARDS = [
  {
    title: "Informe de Ventas",
    desc: "Análisis detallado de ventas por periodo",
    icon: "cart-outline",
    tags: ["Total Ventas", "Ticket Medio", "Productos Vendidos"],
  },
  {
    title: "Informe de Inventario",
    desc: "Estado actual del stock y rotación",
    icon: "package-variant",
    tags: ["Valor Stock", "Productos Bajo Mínimo", "Rotación"],
  },
  {
    title: "Análisis de Productos",
    desc: "Rendimiento por producto y familia",
    icon: "chart-line",
    tags: ["Top Productos", "Margen Beneficio", "Tendencias"],
  },
  {
    title: "Análisis de Clientes",
    desc: "Comportamiento y segmentación de clientes",
    icon: "account-multiple-outline",
    tags: ["Clientes Activos", "LTV", "Retención"],
  },
];

export default function InformesScreen() {
  const [tab, setTab] = useState(0);
  const [tipo, setTipo] = useState(TIPOS[0]);
  const [periodo, setPeriodo] = useState(PERIODOS[0]);
  const [openTipo, setOpenTipo] = useState(false);
  const [openPeriodo, setOpenPeriodo] = useState(false);
  const { data: informe, loading, error, reload } = useFetch(() => informesApi.ventas(), []);
  const { data: productos } = useFetch(() => productosApi.list(), []);
  const { data: clientes } = useFetch(() => clientesApi.list(), []);
  const { data: stocks } = useFetch(() => stocksApi.list(), []);

  const handleExport = () => {
    if (!informe) return showAlert("Informe", "No hay datos para exportar.");
    const cols = ["Concepto", "Cantidad", "Importe"];
    const data = [
      ["Ventas físicas", informe.totalVentasFisicas, informe.importeTotalVentasFisicas],
      ["Pedidos online", informe.totalPedidosOnline, informe.importeTotalPedidosOnline],
      ["Pagos online", informe.totalPagosOnline, informe.importeTotalPagosOnline],
    ];
    if (exportToCsv("informe-ventas", cols, data)) showAlert("Exportado", "informe-ventas.csv descargado.");
    else showAlert("Exportar", "Disponible en la versión web.");
  };

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader title="Centro de Informes y Análisis" />
      <View style={styles.tabs}>
        {TABS.map((t, i) => (
          <Pressable key={t} onPress={() => setTab(i)} style={styles.tabHit}>
            <Text style={[styles.tabTxt, i === tab && styles.tabActive]}>{t}</Text>
            {i === tab ? <View style={styles.und} /> : <View style={{ height: 3 }} />}
          </Pressable>
        ))}
      </View>

      {tab === 0 ? (
        <>
          <Card style={styles.config}>
            <Text style={styles.sectionTitle}>Configuración del Informe</Text>
            <DropdownField
              label="Tipo de Informe"
              value={tipo}
              open={openTipo}
              onOpen={() => {
                setOpenPeriodo(false);
                setOpenTipo(true);
              }}
              onClose={() => setOpenTipo(false)}
              options={TIPOS}
              onSelect={(v) => {
                setTipo(v);
                setOpenTipo(false);
              }}
              focusedBorder={openTipo}
            />
            <DropdownField
              label="Periodo"
              value={periodo}
              open={openPeriodo}
              onOpen={() => {
                setOpenTipo(false);
                setOpenPeriodo(true);
              }}
              onClose={() => setOpenPeriodo(false)}
              options={PERIODOS}
              onSelect={(v) => {
                setPeriodo(v);
                setOpenPeriodo(false);
              }}
              focusedBorder={openPeriodo}
            />
            <View style={{ flexDirection: "row", gap: spacing.sm, marginTop: spacing.md, flexWrap: "wrap" }}>
              <PrimaryButton
                title={loading ? "Cargando..." : "Actualizar informe"}
                disabled={loading}
                onPress={reload}
                icon={<MaterialCommunityIcons name="refresh" size={20} color="#fff" />}
                style={{ flex: 1, minWidth: 140 }}
              />
              <PrimaryButton
                title="Exportar CSV"
                disabled={!informe}
                onPress={handleExport}
                icon={<MaterialCommunityIcons name="download" size={20} color="#fff" />}
                style={{ flex: 1, minWidth: 140 }}
              />
            </View>
            {error ? (
              <View style={styles.note}>
                <Text style={[styles.noteTxt, { color: colors.danger }]}>{error}</Text>
              </View>
            ) : informe ? (
              <View style={styles.note}>
                <Text style={styles.noteTxt}>
                  Ventas físicas: {informe.totalVentasFisicas} ({formatMoney(informe.importeTotalVentasFisicas)})
                </Text>
                <Text style={[styles.noteTxt, { marginTop: 6 }]}>
                  Pedidos online: {informe.totalPedidosOnline} ({formatMoney(informe.importeTotalPedidosOnline)})
                </Text>
                <Text style={[styles.noteTxt, { marginTop: 6 }]}>
                  Pagos online: {informe.totalPagosOnline} ({formatMoney(informe.importeTotalPagosOnline)})
                </Text>
              </View>
            ) : (
              <View style={styles.note}>
                <Text style={styles.noteTxt}>Datos del informe cargados desde /api/informes/ventas</Text>
              </View>
            )}
          </Card>

          <Text style={styles.gridTitle}>Tipos de Informes Disponibles</Text>
          <View style={styles.grid}>
            {REPORT_CARDS.map((r) => (
              <Card key={r.title} style={styles.rcard}>
                <View style={styles.rcHead}>
                  <View style={styles.rcIcon}>
                    <MaterialCommunityIcons name={r.icon} size={22} color={colors.primary} />
                  </View>
                  <Text style={styles.rcTitle}>{r.title}</Text>
                </View>
                <Text style={styles.rcDesc}>{r.desc}</Text>
                <View style={styles.tags}>
                  {r.tags.map((tag) => (
                    <View key={tag} style={styles.tag}>
                      <Text style={styles.tagTxt}>{tag}</Text>
                    </View>
                  ))}
                </View>
              </Card>
            ))}
          </View>
        </>
      ) : tab === 1 ? (
        <Card style={{ padding: spacing.lg }}>
          <Text style={styles.sectionTitle}>Análisis en tiempo real</Text>
          <Text style={styles.noteTxt}>Productos: {productos?.length ?? 0}</Text>
          <Text style={[styles.noteTxt, { marginTop: 6 }]}>Clientes: {clientes?.length ?? 0}</Text>
          <Text style={[styles.noteTxt, { marginTop: 6 }]}>
            Stock bajo: {(stocks || []).filter((s) => s.cantidadDisponible <= s.stockMinimo).length}
          </Text>
          {informe ? (
            <Text style={[styles.noteTxt, { marginTop: 6 }]}>
              Ingresos totales: {formatMoney(
                Number(informe.importeTotalVentasFisicas || 0) + Number(informe.importeTotalPedidosOnline || 0)
              )}
            </Text>
          ) : null}
        </Card>
      ) : (
        <Card style={{ padding: spacing.lg }}>
          <Text style={styles.sectionTitle}>Resumen programado</Text>
          <Text style={styles.noteTxt}>
            Los informes se generan desde los datos actuales del sistema. Usa la pestaña «Generador» para exportar CSV.
          </Text>
          <PrimaryButton title="Refrescar datos" onPress={reload} style={{ marginTop: spacing.md, alignSelf: "flex-start" }} />
        </Card>
      )}
    </ScrollView>
  );
}

function DropdownField({ label, value, open, onOpen, onClose, options, onSelect, focusedBorder }) {
  return (
    <View style={{ marginBottom: spacing.md, zIndex: open ? 20 : 0 }}>
      <Text style={styles.lab}>{label}</Text>
      <Pressable
        onPress={onOpen}
        style={[
          styles.ddField,
          focusedBorder && { borderColor: colors.primary, borderWidth: 2 },
        ]}
      >
        <Text style={styles.ddVal}>{value}</Text>
        <MaterialCommunityIcons name="chevron-down" size={22} color={colors.textMuted} />
      </Pressable>
      <Modal visible={open} transparent animationType="fade" onRequestClose={onClose}>
        <TouchableWithoutFeedback onPress={onClose}>
          <View style={styles.modalBackdrop}>
            <TouchableWithoutFeedback>
              <View style={styles.modalSheet}>
                {options.map((opt) => (
                  <Pressable
                    key={opt}
                    style={({ pressed }) => [styles.opt, pressed && { backgroundColor: colors.activeNavBg }]}
                    onPress={() => onSelect(opt)}
                  >
                    <Text style={styles.optTxt}>{opt}</Text>
                  </Pressable>
                ))}
              </View>
            </TouchableWithoutFeedback>
          </View>
        </TouchableWithoutFeedback>
      </Modal>
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
  config: { marginBottom: spacing.lg },
  sectionTitle: { fontSize: 16, fontWeight: "700", color: colors.text, marginBottom: spacing.md },
  lab: { fontSize: 13, color: colors.textMuted, marginBottom: 6 },
  ddField: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: radius.md,
    paddingHorizontal: 12,
    minHeight: 46,
    backgroundColor: colors.white,
  },
  ddVal: { fontSize: 15, color: colors.text },
  note: {
    marginTop: spacing.md,
    padding: spacing.md,
    backgroundColor: colors.activeNavBg,
    borderRadius: radius.md,
  },
  noteTxt: { fontSize: 13, color: colors.primary, fontWeight: "600" },
  gridTitle: {
    fontSize: 15,
    fontWeight: "700",
    color: colors.textMuted,
    marginBottom: spacing.md,
  },
  grid: {
    flexDirection: "row",
    flexWrap: "wrap",
    gap: spacing.md,
  },
  rcard: {
    flex: 1,
    minWidth: 240,
    maxWidth: Platform.OS === "web" ? 320 : undefined,
  },
  rcHead: { flexDirection: "row", alignItems: "center", gap: spacing.sm, marginBottom: spacing.sm },
  rcIcon: {
    width: 40,
    height: 40,
    borderRadius: radius.sm,
    backgroundColor: colors.activeNavBg,
    alignItems: "center",
    justifyContent: "center",
  },
  rcTitle: { fontSize: 16, fontWeight: "700", color: colors.text, flex: 1 },
  rcDesc: { fontSize: 14, color: colors.textMuted, marginBottom: spacing.md },
  tags: { flexDirection: "row", flexWrap: "wrap", gap: 8 },
  tag: {
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: 999,
    paddingHorizontal: 10,
    paddingVertical: 4,
  },
  tagTxt: { fontSize: 12, color: colors.textMuted },
  modalBackdrop: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.25)",
    justifyContent: "center",
    padding: spacing.lg,
  },
  modalSheet: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    maxHeight: 360,
    ...Platform.select({
      web: { boxShadow: "0 8px 24px rgba(0,0,0,0.12)" },
      default: { elevation: 6 },
    }),
  },
  opt: { paddingVertical: 14, paddingHorizontal: 16 },
  optTxt: { fontSize: 15, color: colors.text },
});
