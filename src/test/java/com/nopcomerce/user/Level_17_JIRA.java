package com.nopcomerce.user;

import commons.BaseTest;
import jiraConfigs.JiraCreateIssue;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.nopCommerce.PageGenerator;
import pageObjects.nopCommerce.users.*;

public class Level_17_JIRA extends BaseTest {
    private static final Logger log = LoggerFactory.getLogger(UserCustomerInfoPO.class);
    //Declare variables
    private WebDriver driver;
    private UserHomePO homePage;
    private UserRegisterPO registerPage;
    private UserLoginPO loginPage;
    private UserCustomerInfoPO customerPage;
    private UserAddressPO addressPage;
    private UserRewardPointPO rewardPointPage;
    private UserOrderPO orderPage;

    String firstName, lastName, day, month, year, email, companyName, password;

    //Pre-condition
    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        driver = getBrowserDriver(browserName);
        //Nó được sinh ra và bắt đầu làm đc các action của page đó
        homePage = PageGenerator.getUserHomePage(driver);
        firstName = "nga";
        lastName = "vu";
        day = "10";
        month = "August";
        year = "1990";
        email = "ngavu" + generateNumber() + "@org.edu";
        companyName = "viettel";
        password = "123456789";
    }

    //Testcase
    @JiraCreateIssue(isCreateIssue = true)
    @Test
    public void User_01_Register() {
//        log.info("User_01_Register - STEP 01: Open register page");
        //ACtion 1
        registerPage = homePage.clickToRegisterLink();
        //Từ homepage qua registerPage
        //Thể hiện trực tiếp trên Test Class
        //-> Sai nguyên tắc trong thiết kế PM/ framework - nguyên tắc đóng gói
//        log.info("User_01_Register - STEP 02: Click to radio button");
        registerPage.clickToMaleRadio();

//        log.info("User_01_Register - STEP 03: Enter to Firstname textbox with value "+firstName);
        registerPage.enterToFirstNameTextbox(firstName);

//        log.info("User_01_Register - STEP 04: Enter to Lastname textbox with value "+lastName);
        registerPage.enterToLastNameTextbox(lastName);

//        log.info("User_01_Register - STEP 05: Enter to Email textbox with value "+email);
        registerPage.enterToEmailTextbox(email);

//        log.info("User_01_Register - STEP 06: Enter to Company Name textbox with value "+companyName);
        registerPage.enterToCompanyTextbox(companyName);

//        log.info("User_01_Register - STEP 07: Enter to Password textbox with value "+password);
        registerPage.enterToPasswordTextbox(password);

//        log.info("User_01_Register - STEP 08: Enter to Password Confirm textbox with value "+password);
        registerPage.enterToPasswordConfirmTextbox(password);

//        log.info("User_01_Register - STEP 09: Click to Register button ");
        registerPage.clickRegisterButton();

//        log.info("User_01_Register - STEP 10: Verify message success is displayed");
        Assert.assertEquals(registerPage.getRegisterSuccessMessage(), "Your registration completed...");
        registerPage.clickToLogoutButton();

    }
    @JiraCreateIssue(isCreateIssue = true)
    @Test
    public void User_02_Login() {
        loginPage = registerPage.clickToLoginButton();
        loginPage.enterToEmailTextbox(email);
        loginPage.enterToPasswordTextbox(password);
        homePage = loginPage.clickLoginToSystem();

        //Từ loginPage qua homePage
        //Page đó đc sinh ra và làm các action
        Assert.assertTrue(homePage.isMyAccountLinkDisplayed());

    }
    @JiraCreateIssue(isCreateIssue = true)
    @Test
    public void User_03_MyAccount() {
        customerPage = homePage.clickToMyaccountLink();
        Assert.assertTrue(customerPage.isGenderMaleSelected());
        Assert.assertEquals(customerPage.getFirstNameTextboxValue(), firstName);

        Assert.assertEquals(customerPage.getLastNameTextboxValue(), lastName);
//        Assert.assertEquals(customerPage.getDayDropdownSelectedValue(),"");
//        Assert.assertEquals(customerPage.getMonthDropdownSelectedValue(),"");
//        Assert.assertEquals(customerPage.getYearDropdownSelectedValue(),"");
        Assert.assertEquals(customerPage.getEmailTextboxValue(), email);
        Assert.assertEquals(customerPage.getCompanyNameTextboxValue(), companyName);
    }
    @JiraCreateIssue(isCreateIssue = true)
    @Test
    public void User_04_Dynamic_Page() {
        //Customer Infor -> Address);
        addressPage = (UserAddressPO) customerPage.openSidebarLinkByPageName("Addresses");

        //Address -> Reward Point
        rewardPointPage= (UserRewardPointPO) addressPage.openSidebarLinkByPageName("Reward points");

        //RewardPoint -> Order
        orderPage= (UserOrderPO) rewardPointPage.openSidebarLinkByPageName("Orders");


        //Order -> Address
        addressPage = (UserAddressPO) orderPage.openSidebarLinkByPageName("Addresses");

        //Address -> Customer Infor
        customerPage= (UserCustomerInfoPO) addressPage.openSidebarLinkByPageName("Customer info");
        addressPage = (UserAddressPO) customerPage.openSidebarLinkByPageName("Addresses");
    }
    @JiraCreateIssue(isCreateIssue = true)
    @Test
    public void User_05_Dynamic_Page() {
        //Address -> Reward Point
        addressPage.openSidebarLinkByPageName("Reward points");
        rewardPointPage= PageGenerator.getUserRewardPointPage(driver);
        //Reward Point -> Order
        rewardPointPage.openSidebarLinkByPageName("Orders");
        orderPage= PageGenerator.getUserOrderPage(driver);

        //Order -> Address
        orderPage.openSidebarLinkByPageName("Addresses");
        addressPage = PageGenerator.getUserAddressPage(driver);

        //Address -> Customer Infor
        addressPage.openSidebarLinkByPageName("Customer info");
        customerPage = PageGenerator.getUserCustomerInfoPage(driver);
    }

    //Post-condition
    @AfterClass
    public void afterClass() {
        driver.quit();
    }
}
