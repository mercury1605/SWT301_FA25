package tests;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest; // Import
import org.junit.jupiter.params.provider.CsvFileSource; // Import
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pages.RegistrationPage;

import java.io.File;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertTrue;

@DisplayName("Tests for DemoQA Registration Form")
public class RegistrationTest extends BaseTest {

    static RegistrationPage registrationPage;
    static WebDriverWait wait;
    static String IMAGE_PATH;

    @BeforeAll
    static void initPage() {
        registrationPage = new RegistrationPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        // Đặt tên file ảnh của bạn ở đây
        IMAGE_PATH = new File("src/test/resources/test-image.jpg").getAbsolutePath();
    }

    @BeforeEach
    void setUpTest() {
        registrationPage.navigate();
    }

    // --- PHẦN 1: GIỮ LẠI CÁC HAPPY PATH ---

    @Test
    @DisplayName("Should submit form successfully with valid data")
    @Order(1)
    void testSuccessfulRegistration() {
        // 2. HÀNH ĐỘNG (Act)
        registrationPage.enterName("John", "Doe");
        registrationPage.enterEmail("john.doe@example.com");
        registrationPage.selectGender("Male");
        registrationPage.enterMobile("0123456789");
        registrationPage.selectHobby("Sports");
        registrationPage.selectSubject("Maths");
        registrationPage.uploadPicture(IMAGE_PATH);
        registrationPage.setDateOfBirthByForce("1 Nov 2025");
        registrationPage.selectState("NCR");
        registrationPage.selectCity("Delhi");

        registrationPage.submit(); // Nhấn submit

        // 3. KIỂM CHỨNG (Assert)
        WebElement modal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registrationPage.getModalTitleLocator())
        );
        String titleText = modal.getText();
        assertTrue(titleText.contains("Thanks for submitting the form"));
    }

    @Test
    @DisplayName("Should submit form successfully when select more hobbies")
    @Order(9)
        // Giữ nguyên thứ tự
    void testSelectMoreHobbies() {
        registrationPage.enterName("John", "Doe");
        registrationPage.enterEmail("john.doe@example.com");
        registrationPage.selectGender("Male");
        registrationPage.enterMobile("0123456789");
        registrationPage.selectSubject("Maths");
        registrationPage.uploadPicture(IMAGE_PATH);

        // Test chọn nhiều Hobbies
        registrationPage.selectHobby("Sports");
        registrationPage.selectHobby("Reading");
        registrationPage.selectHobby("Music");

        registrationPage.setDateOfBirthByForce("1 Nov 2025");
        registrationPage.selectState("NCR");
        registrationPage.selectCity("Delhi");

        registrationPage.submit();

        WebElement modal = wait.until(
                ExpectedConditions.visibilityOfElementLocated(registrationPage.getModalTitleLocator())
        );
        String titleText = modal.getText();
        assertTrue(titleText.contains("Thanks for submitting the form"));
    }

    // --- PHẦN 2: GỘP 7 TEST LỖI VÀO 1 FILE CSV ---

    @ParameterizedTest(name = "Test Validation {index}: {5}")
    @CsvFileSource(resources = "/validation-data.csv", numLinesToSkip = 1) // Đọc file CSV
    @DisplayName("Should show validation errors for required fields")
    @Order(2)
        // Chạy bộ test validation này sau "happy path"
    void testFormValidation(String firstName, String lastName, String gender,
                            String mobile, String email, String expectedLocator) {

        // --- 1. HÀNH ĐỘNG (Act) ---
        // Xử lý giá trị 'null' (trống) từ file CSV
        registrationPage.enterName(
                (firstName == null) ? "" : firstName,
                (lastName == null) ? "" : lastName
        );
        registrationPage.enterEmail((email == null) ? "" : email);
        registrationPage.selectGender((gender == null) ? "" : gender);
        registrationPage.enterMobile((mobile == null) ? "" : mobile);

        registrationPage.submit();

        // --- 2. KIỂM CHỨNG (Assert) ---
        By locator = By.cssSelector(expectedLocator);

        // Nếu là Gender, dùng "presence"
        if (expectedLocator.contains("gender")) {
            WebElement errorField = wait.until(
                    ExpectedConditions.presenceOfElementLocated(locator)
            );
            assertTrue(true, "Error state for gender was found");
        } else {
            // Các trường khác, dùng "visibility"
            WebElement errorField = wait.until(
                    ExpectedConditions.visibilityOfElementLocated(locator)
            );
            assertTrue(errorField.isDisplayed(), "Error indication should be visible for: " + expectedLocator);
        }
    }
}