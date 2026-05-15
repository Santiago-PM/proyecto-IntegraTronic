import React from "react";
import {
  View,
  Text,
  TextInput,
  Modal,
  TouchableWithoutFeedback,
  StyleSheet,
  Platform,
  ScrollView,
  Pressable,
} from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { PrimaryButton, OutlineButton } from "./ui";
import { colors, radius, spacing } from "../constants/theme";

export function FormField({ label, value, onChangeText, placeholder, keyboardType, multiline }) {
  return (
    <View style={fmStyles.field}>
      <Text style={fmStyles.label}>{label}</Text>
      <TextInput
        style={[fmStyles.input, multiline && fmStyles.inputMulti]}
        value={value}
        onChangeText={onChangeText}
        placeholder={placeholder}
        placeholderTextColor={colors.textLight}
        keyboardType={keyboardType}
        multiline={multiline}
      />
    </View>
  );
}

export function SelectField({ label, value, placeholder, onPress }) {
  return (
    <View style={fmStyles.field}>
      <Text style={fmStyles.label}>{label}</Text>
      <Pressable style={fmStyles.select} onPress={onPress}>
        <Text style={value ? fmStyles.selectVal : fmStyles.selectPh}>{value || placeholder}</Text>
        <MaterialCommunityIcons name="chevron-down" size={22} color={colors.textMuted} />
      </Pressable>
    </View>
  );
}

export function FormModal({ visible, title, onClose, onSubmit, submitLabel = "Guardar", loading, error, children }) {
  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <TouchableWithoutFeedback onPress={onClose}>
        <View style={fmStyles.backdrop}>
          <TouchableWithoutFeedback>
            <View style={fmStyles.card}>
              <ScrollView keyboardShouldPersistTaps="handled">
                <Text style={fmStyles.title}>{title}</Text>
                {children}
                {error ? <Text style={fmStyles.error}>{error}</Text> : null}
                <View style={fmStyles.actions}>
                  <OutlineButton title="Cancelar" onPress={onClose} style={{ flex: 1 }} />
                  <PrimaryButton
                    title={loading ? "Guardando..." : submitLabel}
                    onPress={onSubmit}
                    disabled={loading}
                    style={{ flex: 1 }}
                  />
                </View>
              </ScrollView>
            </View>
          </TouchableWithoutFeedback>
        </View>
      </TouchableWithoutFeedback>
    </Modal>
  );
}

export function OptionPicker({ visible, options, onSelect, onClose, title }) {
  return (
    <Modal visible={visible} transparent animationType="fade" onRequestClose={onClose}>
      <TouchableWithoutFeedback onPress={onClose}>
        <View style={fmStyles.backdrop}>
          <TouchableWithoutFeedback>
            <View style={fmStyles.picker}>
              {title ? <Text style={fmStyles.pickerTitle}>{title}</Text> : null}
              {options.map((opt) => (
                <Pressable
                  key={String(opt.value ?? opt.label)}
                  style={({ pressed }) => [fmStyles.opt, pressed && { backgroundColor: colors.activeNavBg }]}
                  onPress={() => onSelect(opt)}
                >
                  <Text style={fmStyles.optTxt}>{opt.label}</Text>
                </Pressable>
              ))}
            </View>
          </TouchableWithoutFeedback>
        </View>
      </TouchableWithoutFeedback>
    </Modal>
  );
}

const fmStyles = StyleSheet.create({
  backdrop: {
    flex: 1,
    backgroundColor: "rgba(0,0,0,0.35)",
    justifyContent: "center",
    padding: spacing.lg,
  },
  card: {
    backgroundColor: colors.white,
    borderRadius: radius.lg,
    padding: spacing.lg,
    maxWidth: 520,
    width: "100%",
    alignSelf: "center",
    maxHeight: "90%",
    ...Platform.select({
      web: { boxShadow: "0 12px 40px rgba(0,0,0,0.15)" },
      default: { elevation: 8 },
    }),
  },
  title: { fontSize: 20, fontWeight: "700", color: colors.text, marginBottom: spacing.md },
  field: { marginBottom: spacing.md },
  label: { fontSize: 13, color: colors.textMuted, marginBottom: 6 },
  input: {
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: radius.md,
    paddingHorizontal: 12,
    paddingVertical: Platform.OS === "web" ? 10 : 8,
    fontSize: 15,
    color: colors.text,
  },
  inputMulti: { minHeight: 72, textAlignVertical: "top" },
  select: {
    flexDirection: "row",
    alignItems: "center",
    justifyContent: "space-between",
    borderWidth: 1,
    borderColor: colors.border,
    borderRadius: radius.md,
    paddingHorizontal: 12,
    minHeight: 44,
  },
  selectVal: { fontSize: 15, color: colors.text, flex: 1 },
  selectPh: { fontSize: 15, color: colors.textLight, flex: 1 },
  error: { color: colors.danger, fontSize: 14, marginBottom: spacing.sm },
  actions: { flexDirection: "row", gap: spacing.sm, marginTop: spacing.sm },
  picker: {
    backgroundColor: colors.white,
    borderRadius: radius.md,
    maxHeight: 360,
    alignSelf: "center",
    minWidth: 280,
    width: "100%",
    maxWidth: 400,
    paddingVertical: spacing.sm,
    ...Platform.select({
      web: { boxShadow: "0 8px 24px rgba(0,0,0,0.12)" },
      default: { elevation: 6 },
    }),
  },
  pickerTitle: {
    fontSize: 16,
    fontWeight: "700",
    paddingHorizontal: spacing.md,
    paddingBottom: spacing.sm,
    color: colors.text,
  },
  opt: { paddingVertical: 14, paddingHorizontal: spacing.md },
  optTxt: { fontSize: 15, color: colors.text },
});
