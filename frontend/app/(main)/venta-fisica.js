import React, { useMemo, useState } from "react";
import { View, Text, StyleSheet, ScrollView, Pressable } from "react-native";
import { MaterialCommunityIcons } from "@expo/vector-icons";
import { Card, PrimaryButton, OutlineButton, PageHeader, ActionLink } from "../../src/components/ui";
import { OptionPicker } from "../../src/components/FormModal";
import { colors, spacing } from "../../src/constants/theme";
import { useFetch } from "../../src/hooks/useFetch";
import { productosApi, ventasApi } from "../../src/services/api";
import { useAuth } from "../../src/context/AuthContext";
import { formatMoney } from "../../src/utils/format";
import { showAlert } from "../../src/utils/alert";

export default function VentaFisicaScreen() {
  const { user } = useAuth();
  const [cart, setCart] = useState([]);
  const [descuento, setDescuento] = useState("0");
  const [pickerOpen, setPickerOpen] = useState(false);
  const [processing, setProcessing] = useState(false);

  const { data: productos } = useFetch(() => productosApi.list(), []);

  const productOptions = useMemo(
    () =>
      (productos || []).map((p) => ({
        label: `${p.sku} — ${p.nombre} (${formatMoney(p.precio)})`,
        value: p.idProducto,
        raw: p,
      })),
    [productos]
  );

  const addProduct = (opt) => {
    const p = opt.raw;
    setCart((prev) => {
      const existing = prev.find((l) => l.idProducto === p.idProducto);
      if (existing) {
        return prev.map((l) =>
          l.idProducto === p.idProducto ? { ...l, cantidad: l.cantidad + 1 } : l
        );
      }
      return [...prev, { idProducto: p.idProducto, nombre: p.nombre, precio: Number(p.precio), cantidad: 1 }];
    });
    setPickerOpen(false);
  };

  const subtotal = cart.reduce((s, l) => s + l.precio * l.cantidad, 0);
  const desc = parseFloat(String(descuento).replace(",", ".")) || 0;
  const base = Math.max(0, subtotal - desc);
  const iva = base * 0.21;
  const total = base + iva;

  const processSale = async () => {
    if (!user?.id) return showAlert("Sesión", "Inicia sesión de nuevo.");
    if (cart.length === 0) return showAlert("Carrito", "Añade al menos un producto.");
    setProcessing(true);
    try {
      await ventasApi.registrar({
        idUsuario: user.id,
        descuento: desc,
        lineas: cart.map((l) => ({ idProducto: l.idProducto, cantidad: l.cantidad })),
      });
      setCart([]);
      setDescuento("0");
      showAlert("Venta registrada", `Total: ${formatMoney(total)}`);
    } catch (e) {
      showAlert("Error", e?.message || "No se pudo registrar la venta.");
    } finally {
      setProcessing(false);
    }
  };

  return (
    <ScrollView style={{ flex: 1 }} contentContainerStyle={styles.scroll}>
      <PageHeader title="Punto de Venta - Tienda Física" />
      <View style={styles.row}>
        <Card style={styles.left}>
          <View style={styles.head}>
            <Text style={styles.cardTitle}>Productos en Venta</Text>
            <OutlineButton
              title="Añadir Producto"
              onPress={() => setPickerOpen(true)}
              icon={<MaterialCommunityIcons name="plus" size={18} color={colors.primary} />}
              style={styles.smallBtn}
            />
          </View>
          <View style={styles.divider} />
          {cart.length === 0 ? (
            <View style={styles.empty}>
              <Text style={styles.emptyText}>Carrito vacío — Añade productos</Text>
            </View>
          ) : (
            cart.map((l) => (
              <View key={l.idProducto} style={styles.line}>
                <Text style={styles.lineName}>{l.nombre}</Text>
                <Text style={styles.lineMeta}>x{l.cantidad} · {formatMoney(l.precio * l.cantidad)}</Text>
                <View style={styles.lineActions}>
                  <Pressable onPress={() => setCart((c) => c.map((x) => x.idProducto === l.idProducto ? { ...x, cantidad: x.cantidad + 1 } : x))}>
                    <Text style={styles.link}>+</Text>
                  </Pressable>
                  <Pressable onPress={() => setCart((c) => c.map((x) => x.idProducto === l.idProducto && x.cantidad > 1 ? { ...x, cantidad: x.cantidad - 1 } : x).filter((x) => x.cantidad > 0))}>
                    <Text style={styles.link}>−</Text>
                  </Pressable>
                  <ActionLink label="Quitar" onPress={() => setCart((c) => c.filter((x) => x.idProducto !== l.idProducto))} />
                </View>
              </View>
            ))
          )}
        </Card>
        <Card style={styles.right}>
          <Text style={styles.cardTitle}>Resumen de Venta</Text>
          <Row k="Subtotal" v={formatMoney(subtotal)} />
          <Row k="Descuento" v={formatMoney(desc)} />
          <Row k="Base Imponible" v={formatMoney(base)} />
          <Row k="IVA (21%)" v={formatMoney(iva)} />
          <Row k="Total" v={formatMoney(total)} bold />
          <PrimaryButton
            title={processing ? "Procesando..." : "Procesar Venta"}
            disabled={processing || cart.length === 0}
            onPress={processSale}
            icon={<MaterialCommunityIcons name="cart" size={20} color="#fff" />}
            style={{ marginTop: spacing.lg }}
          />
          <OutlineButton title="Vaciar Carrito" onPress={() => setCart([])} style={{ marginTop: spacing.sm, borderColor: colors.border }} />
          <View style={styles.itemsBox}>
            <Text style={styles.itemsText}>Artículos: {cart.reduce((n, l) => n + l.cantidad, 0)}</Text>
          </View>
        </Card>
      </View>

      <OptionPicker visible={pickerOpen} title="Añadir producto" onClose={() => setPickerOpen(false)} options={productOptions} onSelect={addProduct} />
    </ScrollView>
  );
}

function Row({ k, v, bold }) {
  return (
    <View style={styles.sumRow}>
      <Text style={styles.sumKey}>{k}</Text>
      <Text style={[styles.sumVal, bold && { color: colors.primary, fontSize: 18, fontWeight: "800" }]}>{v}</Text>
    </View>
  );
}

const styles = StyleSheet.create({
  scroll: { paddingBottom: spacing.xl },
  row: { flexDirection: "row", flexWrap: "wrap", gap: spacing.md },
  left: { flex: 2, minWidth: 300, minHeight: 280 },
  right: { flex: 1, minWidth: 260 },
  head: { flexDirection: "row", justifyContent: "space-between", alignItems: "center", gap: spacing.sm },
  cardTitle: { fontSize: 16, fontWeight: "700", color: colors.text },
  smallBtn: { paddingVertical: 8, paddingHorizontal: 10 },
  divider: { height: 1, backgroundColor: colors.border, marginVertical: spacing.md },
  empty: { minHeight: 120, alignItems: "center", justifyContent: "center" },
  emptyText: { color: colors.textMuted },
  line: { paddingVertical: spacing.sm, borderBottomWidth: 1, borderBottomColor: colors.border },
  lineName: { fontWeight: "600", color: colors.text },
  lineMeta: { color: colors.textMuted, fontSize: 13, marginTop: 2 },
  lineActions: { flexDirection: "row", gap: spacing.md, marginTop: 6, alignItems: "center" },
  link: { color: colors.primary, fontWeight: "700", fontSize: 16 },
  sumRow: { flexDirection: "row", justifyContent: "space-between", marginTop: spacing.sm },
  sumKey: { color: colors.textMuted, fontSize: 14 },
  sumVal: { color: colors.text, fontSize: 15, fontWeight: "600" },
  itemsBox: { marginTop: spacing.lg, padding: spacing.md, backgroundColor: "#F3F4F6", borderRadius: 8 },
  itemsText: { color: colors.textMuted, fontSize: 13 },
});
