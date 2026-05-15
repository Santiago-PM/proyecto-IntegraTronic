import React, { useMemo, useState } from "react";
import { View, Text, Pressable, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import {
  PrimaryButton,
  SearchField,
  TableShell,
  PaginationFooter,
  PageHeader,
  StatMini,
  ActionLink,
} from "../../src/components/ui";
import { FormModal, FormField } from "../../src/components/FormModal";
import { colors, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { clientesApi } from "../../src/services/api";
import { exportToCsv } from "../../src/utils/exportCsv";
import { showAlert, confirmAction } from "../../src/utils/alert";

const TABS = ["TODOS", "CON EMAIL", "CON TELÉFONO"];

export default function ClientesScreen() {
  const [tab, setTab] = useState(0);
  const [q, setQ] = useState("");
  const [modal, setModal] = useState(false);
  const [form, setForm] = useState({ nombre: "", email: "", telefono: "" });
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const { data: clientes, loading, error, reload } = useFetch(() => clientesApi.list(), []);

  const filtered = useMemo(() => {
    let list = clientes || [];
    if (tab === 1) list = list.filter((c) => c.email);
    if (tab === 2) list = list.filter((c) => c.telefono);
    const term = q.trim().toLowerCase();
    if (!term) return list;
    return list.filter(
      (c) =>
        c.nombre?.toLowerCase().includes(term) ||
        c.email?.toLowerCase().includes(term) ||
        c.telefono?.toLowerCase().includes(term)
    );
  }, [clientes, q, tab]);

  const handleExport = () => {
    const cols = ["Nombre", "Email", "Teléfono"];
    const data = filtered.map((c) => [c.nombre, c.email, c.telefono]);
    if (exportToCsv("clientes", cols, data)) showAlert("Exportado", "Archivo clientes.csv descargado.");
    else showAlert("Exportar", "Disponible en la versión web.");
  };

  const handleSave = async () => {
    setFormError(null);
    if (!form.nombre.trim() || !form.email.trim()) return setFormError("Nombre y email son obligatorios.");
    setSaving(true);
    try {
      await clientesApi.create({
        nombre: form.nombre.trim(),
        email: form.email.trim(),
        telefono: form.telefono.trim() || null,
      });
      setModal(false);
      reload();
      showAlert("Listo", "Cliente creado.");
    } catch (e) {
      setFormError(e?.message || "Error al guardar.");
    } finally {
      setSaving(false);
    }
  };

  const rows = filtered.map((c) => ({
    key: c.idCliente,
    cells: [
      c.nombre || "—",
      c.email || "—",
      c.telefono || "—",
      "—",
      "—",
      "—",
      <ActionLink key="d" onPress={async () => {
        if (await confirmAction("Eliminar", "¿Eliminar cliente?")) {
          try { await clientesApi.remove(c.idCliente); reload(); } catch (e) { showAlert("Error", e?.message); }
        }
      }} />,
    ],
  }));

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Gestión de Clientes"
        subtitle="Base de datos de clientes"
        right={
          <View style={styles.headerActions}>
            <PrimaryButton title="Exportar" onPress={handleExport} icon={<MaterialCommunityIcons name="download" size={18} color="#fff" />} style={{ paddingVertical: 8, backgroundColor: colors.textMuted }} />
            <PrimaryButton title="Nuevo Cliente" onPress={() => { setForm({ nombre: "", email: "", telefono: "" }); setModal(true); }} icon={<MaterialCommunityIcons name="plus" size={20} color="#fff" />} />
          </View>
        }
      />
      <View style={styles.stats}>
        <StatMini label="Total" value={String(clientes?.length ?? 0)} iconBg={colors.primary} icon={<MaterialCommunityIcons name="folder-account" size={20} color="#fff" />} />
        <StatMini label="Mostrando" value={String(filtered.length)} iconBg={colors.orange} icon={<MaterialCommunityIcons name="chart-line" size={20} color="#fff" />} />
      </View>
      <View style={styles.tabs}>
        {TABS.map((t, i) => (
          <Pressable key={t} onPress={() => setTab(i)}>
            <Text style={[styles.tabTxt, i === tab && styles.tabTxtActive]}>{t}</Text>
            {i === tab ? <View style={styles.tabUnderline} /> : <View style={{ height: 2 }} />}
          </Pressable>
        ))}
      </View>
      <SearchField placeholder="Buscar..." value={q} onChangeText={setQ} />
      <TableShell columns={["Cliente", "Email", "Teléfono", "—", "—", "—", "Acciones"]} rows={rows} loading={loading} error={error} emptyText="No hay clientes" footer={<PaginationFooter total={rows.length} />} />

      <FormModal visible={modal} title="Nuevo cliente" onClose={() => setModal(false)} onSubmit={handleSave} loading={saving} error={formError}>
        <FormField label="Nombre *" value={form.nombre} onChangeText={(v) => setForm((f) => ({ ...f, nombre: v }))} />
        <FormField label="Email *" value={form.email} onChangeText={(v) => setForm((f) => ({ ...f, email: v }))} />
        <FormField label="Teléfono" value={form.telefono} onChangeText={(v) => setForm((f) => ({ ...f, telefono: v }))} />
      </FormModal>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  headerActions: { flexDirection: "row", gap: spacing.sm, flexWrap: "wrap" },
  stats: { flexDirection: "row", flexWrap: "wrap", gap: spacing.md, marginBottom: spacing.lg },
  tabs: { flexDirection: "row", gap: spacing.lg, marginBottom: spacing.md },
  tabTxt: { fontSize: 12, fontWeight: "700", color: colors.textMuted },
  tabTxtActive: { color: colors.primary },
  tabUnderline: { marginTop: 6, height: 3, backgroundColor: colors.primary, borderRadius: 2 },
});
