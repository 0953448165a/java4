import java.util.Objects;

public class ContractEmployee extends Employee {

    private int contractMonths;
    private String agency;


    public ContractEmployee(String firstName, String lastName, String position,
                            double salary, int age, Department department,
                            int contractMonths, String agency) {
        super(firstName, lastName, position, salary, age, department);
        setContractMonths(contractMonths);
        setAgency(agency);
    }

    /** @return строк контракту в місяцях */
    public int getContractMonths() {
        return contractMonths;
    }

    public void setContractMonths(int contractMonths) {
        if (contractMonths < 1 || contractMonths > 60) {
            throw new IllegalArgumentException("Строк контракту має бути від 1 до 60 місяців.");
        }
        this.contractMonths = contractMonths;
    }

    /** @return назва агенції */
    public String getAgency() {
        return agency;
    }

    public void setAgency(String agency) {
        if (agency == null || agency.isBlank()) {
            throw new IllegalArgumentException("Назва агенції не може бути порожньою.");
        }
        this.agency = agency.trim();
    }

    /** @return сумарна вартість контракту за весь строк */
    public double getTotalContractCost() {
        return calculateMonthlyPay() * contractMonths;
    }

    /** @return назва типу працівника */
    @Override
    public String getEmployeeType() {
        return "Працівник за контрактом";
    }

    /** @return текстове подання працівника за контрактом з усіма полями */
    @Override
    public String toString() {
        return "ContractEmployee{" + baseFieldsToString() +
                ", contractMonths=" + contractMonths +
                ", agency='" + agency + "'}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!super.equals(o)) return false; // перевіряє також, що клас однаковий
        ContractEmployee that = (ContractEmployee) o;
        return contractMonths == that.contractMonths
                && Objects.equals(agency, that.agency);
    }

    /** @return хеш-код, узгоджений з {@link #equals(Object)} */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), contractMonths, agency);
    }
}