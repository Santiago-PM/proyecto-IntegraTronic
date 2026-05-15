import React, { useState } from "react";
import { View, Text, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { PrimaryButton, OutlineButton, Card, PageHeader, ActionLink } from "../../src/components/ui";
import { FormModal, FormField } from "../../src/components/FormModal";
import { colors, radius, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { familiasApi } from "../../src/services/api";
import { showAlert, confirmAction } from "../../src/utils/alert";

export default function FamiliasScreen() {
  const [modal, setModal] = useState(false);
  const [form, setForm] = useState({ nombre: "", descripcion: "" });
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const { data: familias, loading, error, reload } = useFetch(() => familiasApi.list(), []);

  const handleSave = async () => {
    if (!form.nombre.trim()) return setFormError("El nombre es obligatorio.");
    setSaving(true);
    try {
      await familiasApi.create({ nombre: form.nombre.trim(), descripcion: form.descripcion.trim() || null });
      setModal(false);
      reload();
      showAlert("Listo", "Familia creada.");
    } catch (e) {
      setFormError(e?.message || "Error al guardar.");
    } finally {
      setSaving(false);
    }
  };

  const openCreate = () => {
    setForm({ nombre: "", descripcion: "" });
    setFormError(null);
    setModal(true);
  };

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Gestión de Familias de Productos"
        right={
          <PrimaryButton title="Nueva Familia" onPress={openCreate} icon={<MaterialCommunityIcons name="plus" size={20} color="#fff" />} />
        }
      />
      {loading ? (
        <Card style={styles.emptyCard}><Text style={styles.emptySub}>Cargando...</Text></Card>
      ) : error ? (
        <Card style={styles.emptyCard}><Text style={[styles.emptySub, { color: colors.danger }]}>{error}</Text></Card>
      ) : (familias?.length ?? 0) > 0 ? (
        <View style={styles.grid}>
          {familias.map((f) => (
            <Card key={f.idFamilia} style={styles.familyCard}>
              <View style={styles.familyHead}>
                <MaterialCommunityIcons name="folder-outline" size={24} color={colors.primary} />
                <Text style={styles.familyTitle}>{f.nombre}</Text>
                <ActionLink
                  onPress={async () => {
                    if (await confirmAction("Eliminar", "¿Eliminar familia?")) {
                      try { await familiasApi.remove(f.idFamilia); reload(); } catch (e) { showAlert("Error", e?.message); }
                    }
                  }}
                />
              </View>
              <Text style={styles.familyDesc}>{f.descripcion || "Sin descripción"}</Text>
            </Card>
          ))}
        </View>
      ) : (
        <Card style={styles.emptyCard}>
          <MaterialCommunityIcons name="folder-multiple-outline" size={64} color="#D1D5DB" />
          <Text style={styles.emptyTitle}>No hay familias</Text>
          <OutlineButton title="Crear Primera Familia" onPress={openCreate} icon={<MaterialCommunityIcons name="plus" size={18} color={colors.primary} />} style={{ marginTop: spacing.lg }} />
        </Card>
      )}

      <FormModal visible={modal} title="Nueva familia" onClose={() => setModal(false)} onSubmit={handleSave} loading={saving} error={formError}>
        <FormField label="Nombre *" value={form.nombre} onChangeText={(v) => setForm((f) => ({ ...f, nombre: v }))} />
        <FormField label="Descripción" value={form.descripcion} onChangeText={(v) => setForm((f) => ({ ...f, descripcion: v }))} multiline />
      </FormModal>
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  grid: { flexDirection: "row", flexWrap: "wrap", gap: spacing.md },
  familyCard: { flex: 1, minWidth: 220, maxWidth: 360 },
  familyHead: { flexDirection: "row", alignItems: "center", gap: spacing.sm, marginBottom: spacing.sm },
  familyTitle: { fontSize: 17, fontWeight: "700", color: colors.text, flex: 1 },
  familyDesc: { fontSize: 14, color: colors.textMuted },
  emptyCard: { minHeight: 280, alignItems: "center", justifyContent: "center", paddingVertical: spacing.xl },
  emptyTitle: { marginTop: spacing.md, fontSize: 18, fontWeight: "700", color: colors.text },
  emptySub: { marginTop: spacing.sm, fontSize: 14, color: colors.textMuted },
});
