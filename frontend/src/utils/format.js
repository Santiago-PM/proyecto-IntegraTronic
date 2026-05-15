export function formatMoney(value) {
  const n = Number(value);
  if (Number.isNaN(n)) return "€0.00";
  return `€${n.toFixed(2)}`;
}

export function formatDate(value) {
  if (!value) return "—";
  const d = new Date(value);
  if (Number.isNaN(d.getTime())) return String(value);
  return d.toLocaleDateString("es-ES");
}
