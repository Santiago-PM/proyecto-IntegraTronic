import React, { useMemo, useState } from "react";
import { View, StyleSheet, ScrollView } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { PrimaryButton, TableShell, PaginationFooter, PageHeader, ActionLink } from "../../src/components/ui";
import { FormModal, FormField, SelectField, OptionPicker } from "../../src/components/FormModal";
import { spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { usuariosApi, rolesApi } from "../../src/services/api";
import { showAlert, confirmAction } from "../../src/utils/alert";

export default function UsuariosScreen() {
  const [modal, setModal] = useState(false);
  const [pickerRol, setPickerRol] = useState(false);
  const [form, setForm] = useState({ nombre: "", email: "", password: "", idRol: null, nombreRol: "" });
  const [saving, setSaving] = useState(false);
  const [formError, setFormError] = useState(null);

  const { data: usuarios, loading, error, reload } = useFetch(() => usuariosApi.list(), []);
  const { data: roles } = useFetch(() => rolesApi.list(), []);

  const handleSave = async () => {
    setFormError(null);
    if (!form.nombre.trim() || !form.email.trim() || !form.password) return setFormError("Completa todos los campos.");
    if (!form.idRol) return setFormError("Selecciona un rol.");
    setSaving(true);
    try {
      await usuariosApi.create({
        nombre: form.nombre.trim(),
        email: form.email.trim(),
        passwordHash: form.password,
        activo: true,
        idRol: form.idRol,
      });
      setModal(false);
      reload();
      showAlert("Listo", "Usuario creado.");
    } catch (e) {
      setFormError(e?.message || "Error al guardar.");
    } finally {
      setSaving(false);
    }
  };

  const rows = useMemo(
    () =>
      (usuarios || []).map((u) => ({
        key: u.idUsuario,
        cells: [
          String(u.idUsuario),
          u.nombre || "—",
          u.email || "—",
          u.nombreRol || "—",
          u.activo ? "Activo" : "Inactivo",
          <ActionLink key="d" onPress={async () => {
            if (await confirmAction("Eliminar", "¿Eliminar usuario?")) {
              try { await usuariosApi.remove(u.idUsuario); reload(); } catch (e) { showAlert("Error", e?.message); }
            }
          }} />,
        ],
      })),
    [usuarios, reload]
  );

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader
        title="Gestión de Usuarios"
        right={
          <PrimaryButton
            title="Nuevo Usuario"
            onPress={() => {
              setForm({ nombre: "", email: "", password: "", idRol: null, nombreRol: "" });
              setModal(true);
            }}
            icon={<MaterialCommunityIcons name="plus" size={20} color="#fff" />}
          />
        }
      />
      <TableShell columns={["ID", "Nombre", "Email", "Rol", "Estado", "Acciones"]} rows={rows} loading={loading} error={error} emptyText="No hay usuarios" footer={<PaginationFooter total={rows.length} />} />

      <FormModal visible={modal} title="Nuevo usuario" onClose={() => setModal(false)} onSubmit={handleSave} loading={saving} error={formError}>
        <FormField label="Nombre *" value={form.nombre} onChangeText={(v) => setForm((f) => ({ ...f, nombre: v }))} />
        <FormField label="Email *" value={form.email} onChangeText={(v) => setForm((f) => ({ ...f, email: v }))} />
        <FormField label="Contraseña *" value={form.password} onChangeText={(v) => setForm((f) => ({ ...f, password: v }))} />
        <SelectField label="Rol *" value={form.nombreRol} placeholder="Seleccionar..." onPress={() => setPickerRol(true)} />
      </FormModal>

      <OptionPicker
        visible={pickerRol}
        title="Rol"
        onClose={() => setPickerRol(false)}
        options={(roles || []).map((r) => ({ label: r.nombreRol, value: r.idRol }))}
        onSelect={(opt) => {
          setForm((f) => ({ ...f, idRol: opt.value, nombreRol: opt.label }));
          setPickerRol(false);
        }}
      />
    </ScrollView>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
});
