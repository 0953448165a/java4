import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UuidSearchTest {

    @TempDir
    Path tempDir;

    @Test
    void employeeShouldImplementIdentifiableAndGenerateUniqueUuid() {
        Employee first = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000, 25, Department.IT, 5000, 24);
        Employee second = new FullTimeEmployee("Ярослав", "Мудрий", "Розробник",
                30000, 25, Department.IT, 5000, 24);

        assertTrue(first instanceof Identifiable);
        assertNotNull(first.getUuid());
        assertNotNull(second.getUuid());
        assertNotEquals(first.getUuid(), second.getUuid());
        assertTrue(first.toString().contains(first.getUuid().toString()));
        assertTrue(first.toShortString().contains(first.getUuid().toString()));
    }

    @Test
    void shouldFindEmployeeByValidUuidStringAndObject() {
        Company company = new SampleData().company();
        Employee expected = company.getEmployees().get(2);
        UUID targetUuid = expected.getUuid();

        assertSame(expected, company.searchByUuid(targetUuid));
        assertSame(expected, company.searchByUuid("  " + targetUuid.toString() + "  "));
    }

    @Test
    void shouldReturnNullWhenUuidNotFound() {
        Company company = new SampleData().company();
        UUID unknown = UUID.randomUUID();

        assertNull(company.searchByUuid(unknown));
        assertNull(company.searchByUuid(unknown.toString()));
    }

    @Test
    void shouldThrowIllegalArgumentExceptionForInvalidUuidFormat() {
        Company company = new SampleData().company();

        assertThrows(IllegalArgumentException.class, () -> company.searchByUuid("not-a-valid-uuid"));
        assertThrows(IllegalArgumentException.class, () -> company.searchByUuid(""));
        assertThrows(IllegalArgumentException.class, () -> company.searchByUuid("   "));
    }

    @Test
    void shouldPreserveUuidAcrossTextStorageSaveAndLoad() {
        TextStorage storage = new TextStorage(tempDir.resolve("uuid_company.txt").toString());
        Company original = new SampleData().company();
        UUID originalUuid = original.getEmployees().get(1).getUuid();

        storage.saveCompany(original);
        Company loaded = storage.loadCompany();

        Employee found = loaded.searchByUuid(originalUuid);
        assertNotNull(found);
        assertEquals(originalUuid, found.getUuid());
        assertEquals(original.getEmployees().get(1), found);
    }
}