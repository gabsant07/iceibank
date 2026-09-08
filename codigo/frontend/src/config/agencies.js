export const agencies = [
  {
    id: 0,
    name: "Vila Mariana",
    label: "Agência 0",
    baseUrl: import.meta.env.VITE_AGENCY_0_URL || "http://localhost:8080"
  },
  {
    id: 1,
    name: "Paulista",
    label: "Agência 1",
    baseUrl: import.meta.env.VITE_AGENCY_1_URL || "http://localhost:8081"
  },
  {
    id: 2,
    name: "Perdizes",
    label: "Agência 2",
    baseUrl: import.meta.env.VITE_AGENCY_2_URL || "http://localhost:8082"
  }
];

export function agencyForAccount(accountNumber) {
  const value = Number(accountNumber);
  return agencies[((value % 3) + 3) % 3];
}
