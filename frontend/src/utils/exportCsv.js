import { Platform } from "react-native";

export function exportToCsv(filename, columns, rows) {
  const header = columns.map((c) => `"${String(c).replace(/"/g, '""')}"`).join(",");
  const body = rows
    .map((cells) =>
      cells.map((cell) => `"${String(cell ?? "").replace(/"/g, '""')}"`).join(",")
    )
    .join("\n");
  const csv = `\uFEFF${header}\n${body}`;

  if (Platform.OS === "web" && typeof document !== "undefined") {
    const blob = new Blob([csv], { type: "text/csv;charset=utf-8;" });
    const url = URL.createObjectURL(blob);
    const link = document.createElement("a");
    link.href = url;
    link.download = filename.endsWith(".csv") ? filename : `${filename}.csv`;
    link.click();
    URL.revokeObjectURL(url);
    return true;
  }
  return false;
}
