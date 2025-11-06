package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

public class RegistrationPage extends BasePage {

    // Thêm vào bên trong class RegistrationPage
    //locator
    private By firstNameField = By.id("firstName");
    private By lastNameField = By.id("lastName");
    private By emailField = By.id("userEmail");
    private By genderMaleRadio = By.cssSelector("label[for='gender-radio-1']");
    private By genderFemaleRadio = By.cssSelector("label[for='gender-radio-2']");
    private By genderOtherRadio = By.cssSelector("label[for='gender-radio-3']");
    private By mobileField = By.id("userNumber");
    private By dobField = By.id("dateOfBirthInput");
    // Các locators này CHỈ xuất hiện sau khi click vào dobField
    private By monthSelect = By.cssSelector(".react-datepicker__month-select");
    private By yearSelect = By.cssSelector(".react-datepicker__year-select");
    // %s là một placeholder
    private By daySelect = By.cssSelector(".react-datepicker__day--0%s:not(.react-datepicker__day--outside-month)");
    private By subjectsField = By.id("subjectsInput");
    private By hobbiesSportsCheck = By.cssSelector("label[for='hobbies-checkbox-1']");
    private By hobbiesReadingCheck = By.cssSelector("label[for='hobbies-checkbox-2']");
    private By hobbiesMusicCheck = By.cssSelector("label[for='hobbies-checkbox-3']");
    private By pictureUpload = By.id("uploadPicture");
    private By addressField = By.id("currentAddress");
    private By stateDropdown = By.id("state");
    private By cityDropdown = By.id("city");
    // Thêm vào khu vực Locators
    private By stateContainer = By.id("state");
    private By stateInput = By.id("react-select-3-input"); // ID này có thể thay đổi, dùng "Inspect" kỹ
    private By cityContainer = By.id("city");
    private By cityInput = By.id("react-select-4-input"); // ID này có thể thay đổi
    private By submitButton = By.id("submit");

    // Locator cho pop-up kết quả (sau khi submit thành công)
    private By modalTitle = By.id("example-modal-sizes-title-lg");


    public RegistrationPage(WebDriver driver) {
        super(driver);
    }


    //1. navigate to URL
    public void navigate() {
        navigateTo("https://demoqa.com/automation-practice-form");
    }

    // 2. fill full name
    public void enterName(String firstName, String lastName) {
        type(firstNameField, firstName);
        type(lastNameField, lastName);
    }

    // 3. fill email
    public void enterEmail(String email) {
        type(emailField, email);
    }


    //4. select gender
    public void selectGender(String gender) {
        if (gender.equalsIgnoreCase("Male")) {
            click(genderMaleRadio);
        } else if (gender.equalsIgnoreCase("Female")) {
            click(genderFemaleRadio);
        } else if (gender.equalsIgnoreCase("Other")) {
            click(genderOtherRadio);
        }
    }

    //5. fill phone num
    public void enterMobile(String mobile) {
        type(mobileField, mobile);
    }


    //6. choose hobby
    public void selectHobby(String hobby) {
        if (hobby.equalsIgnoreCase("Sports")) {
            click(hobbiesSportsCheck);
        } else if (hobby.equalsIgnoreCase("Reading")) {
            click(hobbiesReadingCheck);
        } else {
            click(hobbiesMusicCheck);
        }
    }

    //7. click submit
    public void submit() {
        click(submitButton);
    }

    //8. get title of modal
    public By getModalTitleLocator() {
        return modalTitle;
    }

    public String getModalTitleText() {
        return getText(modalTitle);
    }

    //9. select dob
    public void selectDateOfBirth(String day, String month, String year) {
        click(dobField);
        WebElement monthDropdown = waitForVisibility(monthSelect);
        new Select(monthDropdown).selectByVisibleText(month);
        WebElement yearDropdown = waitForVisibility(yearSelect);
        new Select(yearDropdown).selectByValue(year);
        String formattedDay = String.format("%02d", Integer.parseInt(day));
        By dayLocator = By.cssSelector(String.format(".react-datepicker__day--%s:not(.react-datepicker__day--outside-month)", formattedDay));
        click(dayLocator);
    }

    public void setDateOfBirthByForce(String date) { // ví dụ: "06 Nov 2025"
        WebElement dobInput = driver.findElement(dobField);
        // Dùng tổ hợp phím để chọn tất cả (Ctrl+A) và gõ đè
        dobInput.sendKeys(Keys.chord(Keys.CONTROL, "a"));
        dobInput.sendKeys(date);
        dobInput.sendKeys(Keys.ENTER);
    }

    //9. select state, city
    public void selectFromDynamicDropdown(By containerLocator, By inputLocator, String optionText) {
        click(containerLocator);
        WebElement input = waitForVisibility(inputLocator);
        input.sendKeys(optionText);
        input.sendKeys(Keys.ENTER);
    }

    // Bây giờ, bạn tạo 2 hàm cụ thể gọi hàm chung ở trên:
    public void selectState(String stateName) {
        selectFromDynamicDropdown(stateContainer, stateInput, stateName);
    }

    public void selectCity(String cityName) {
        selectFromDynamicDropdown(cityContainer, cityInput, cityName);
    }


    public void selectSubject(String subjectName) {
        WebElement subjectInput = waitForVisibility(subjectsField);
        subjectInput.sendKeys(subjectName);
        subjectInput.sendKeys(Keys.ENTER);
    }


    public void uploadPicture(String absoluteFilePath) {
        WebElement uploadElement = driver.findElement(pictureUpload);
        uploadElement.sendKeys(absoluteFilePath);
    }

}
