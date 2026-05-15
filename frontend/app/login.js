import React, { useState, useEffect } from "react";
import {
  View,
  Text,
  TextInput,
  Pressable,
  StyleSheet,
  KeyboardAvoidingView,
  Platform,
  ScrollView,
} from "react-native";
import { router } from "expo-router";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { useSafeAreaInsets } from "react-native-safe-area-context";
import { useAuth } from "../src/context/AuthContext";
import { colors, radius, spacing } from "../src/constants/theme";
import { APP_NAME, APP_TAGLINE } from "../src/constants/app";

export default function LoginScreen() {
  const insets = useSafeAreaInsets();
  const { signIn, user, authError, loading } = useAuth();
  const [usuario, setUsuario] = useState("");
  const [password, setPassword] = useState("");
  const [showPass, setShowPass] = useState(false);
  const [remember, setRemember] = useState(false);

  useEffect(() => {
    if (user) router.replace("/dashboard");
  }, [user]);

  return (
    <KeyboardAvoidingView
      style={[styles.root, { paddingTop: insets.top, paddingBottom: insets.bottom }]}
      behavior={Platform.OS === "ios" ? "padding" : undefined}
    >
      <ScrollView contentContainerStyle={styles.scroll} keyboardShouldPersistTaps="handled">
        <View style={styles.card}>
          <View style={styles.logoCircle}>
            <MaterialCommunityIcons name="login" size={28} color={colors.white} />
          </View>
          <Text style={styles.brand}>{APP_NAME}</Text>
          <Text style={styles.sub}>{APP_TAGLINE}</Text>

          <TextInput
            style={styles.input}
            placeholder="Usuario"
            placeholderTextColor="#888888"
            value={usuario}
            onChangeText={setUsuario}
            autoCapitalize="none"
          />
          <View style={styles.passWrap}>
            <TextInput
              style={[styles.input, styles.inputPass]}
              placeholder="Contraseña"
              placeholderTextColor="#888888"
              value={password}
              onChangeText={setPassword}
              secureTextEntry={!showPass}
            />
            <Pressable style={styles.eye} onPress={() => setShowPass((v) => !v)}>
              <MaterialCommunityIcons
                name={showPass ? "eye-off-outline" : "eye-outline"}
                size={22}
                color="#888888"
              />
            </Pressable>
          </View>

          <Pressable
            style={styles.checkRow}
            onPress={() => setRemember((r) => !r)}
            accessibilityRole="checkbox"
            accessibilityState={{ checked: remember }}
          >
            <View style={[styles.checkbox, remember && styles.checkboxOn]}>
              {remember ? <Text style={styles.checkMark}>✓</Text> : null}
            </View>
            <Text style={styles.checkLabel}>Recordar sesión</Text>
          </Pressable>

          {authError ? <Text style={styles.error}>{authError}</Text> : null}

          <Pressable
            style={({ pressed }) => [styles.cta, pressed && { opacity: 0.92 }, loading && { opacity: 0.7 }]}
            disabled={loading}
            onPress={async () => {
              const ok = await signIn(usuario.trim(), password);
              if (ok) router.replace("/dashboard");
            }}
          >
            <Text style={styles.ctaText}>{loading ? "Conectando..." : "Iniciar Sesión"}</Text>
          </Pressable>

          <Text style={styles.footer}>
            <Text style={styles.footerMuted}>¿Olvidaste tu contraseña? </Text>
            <Text style={styles.footerLink}>Recuperar</Text>
          </Text>
        </View>
      </ScrollView>
    </KeyboardAvoidingView>
  );
}

const styles = StyleSheet.create({
  root: {
    flex: 1,
    backgroundColor: colors.loginBg,
  },
  scroll: {
    flexGrow: 1,
    justifyContent: "center",
    padding: spacing.lg,
  },
  card: {
    alignSelf: "center",
    width: "100%",
    maxWidth: 400,
    backgroundColor: colors.white,
    borderRadius: radius.lg,
    paddingVertical: spacing.xl + 8,
    paddingHorizontal: spacing.xl,
    ...Platform.select({
      web: {
        boxShadow: "0 8px 24px rgba(0,0,0,0.08)",
      },
      default: {
        elevation: 4,
      },
    }),
  },
  logoCircle: {
    width: 56,
    height: 56,
    borderRadius: 28,
    backgroundColor: colors.primaryLogin,
    alignSelf: "center",
    alignItems: "center",
    justifyContent: "center",
    marginBottom: spacing.md,
  },
  brand: {
    textAlign: "center",
    fontSize: 22,
    fontWeight: "700",
    color: "#333333",
  },
  sub: {
    textAlign: "center",
    marginTop: 6,
    marginBottom: spacing.lg,
    fontSize: 14,
    color: "#888888",
  },
  input: {
    borderWidth: 1,
    borderColor: colors.borderInput,
    borderRadius: radius.md,
    paddingHorizontal: 14,
    paddingVertical: Platform.OS === "web" ? 12 : 10,
    fontSize: 16,
    color: "#333333",
    marginBottom: spacing.md,
  },
  passWrap: {
    position: "relative",
    marginBottom: spacing.md,
  },
  inputPass: {
    marginBottom: 0,
    paddingRight: 44,
  },
  eye: {
    position: "absolute",
    right: 10,
    top: Platform.OS === "web" ? 10 : 8,
    padding: 4,
  },
  checkRow: {
    flexDirection: "row",
    alignItems: "center",
    marginBottom: spacing.lg,
  },
  checkbox: {
    width: 18,
    height: 18,
    borderWidth: 1,
    borderColor: colors.borderInput,
    borderRadius: 4,
    marginRight: 10,
    alignItems: "center",
    justifyContent: "center",
  },
  checkboxOn: {
    backgroundColor: colors.primaryLogin,
    borderColor: colors.primaryLogin,
  },
  checkMark: {
    color: colors.white,
    fontSize: 12,
    fontWeight: "700",
  },
  checkLabel: {
    fontSize: 14,
    color: "#333333",
  },
  cta: {
    backgroundColor: colors.primaryLogin,
    borderRadius: radius.md,
    paddingVertical: 14,
    alignItems: "center",
    marginBottom: spacing.lg,
  },
  ctaText: {
    color: colors.white,
    fontWeight: "700",
    fontSize: 16,
  },
  footer: {
    textAlign: "center",
    fontSize: 14,
  },
  footerMuted: {
    color: "#888888",
  },
  footerLink: {
    color: colors.primaryLogin,
    fontWeight: "600",
  },
  error: {
    color: "#DC2626",
    fontSize: 14,
    marginBottom: spacing.md,
    textAlign: "center",
  },
});
