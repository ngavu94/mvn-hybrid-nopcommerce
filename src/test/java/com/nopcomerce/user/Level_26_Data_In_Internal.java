package com.nopcomerce.user;

import commons.BaseTest;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import pageObjects.nopCommerce.PageGenerator;
import pageObjects.nopCommerce.users.*;
import staticVariable.nopcomerce.user.UserObject;

import java.util.Random;

public class Level_26_Data_In_Internal extends BaseTest {
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
    private String browserName;

    String day, month, year, companyName;

    //Pre-condition
    @Parameters("browser")
    @BeforeClass
    public void beforeClass(String browserName) {
        this.browserName = browserName;
//        this.email = getEmailRandom(UserObject.EMAIL_ADDRESS);
//        this.firstName = UserObject.FIRST_NAME;
//        this.lastName = UserObject.LAST_NAME;
//        this.password = UserObject.PASSWORD;
        driver = getBrowserDriver(browserName);
        //Nó được sinh ra và bắt đầu làm đc các action của page đó
        homePage = PageGenerator.getUserHomePage(driver);

    }

    //Testcase
    @Test
    public void User_01_Register() {
        log.info("User_01_Register - STEP 01: Open register page");
        //ACtion 1
        registerPage = homePage.clickToRegisterLink();
        //Từ homepage qua registerPage
        //Thể hiện trực tiếp trên Test Class
        //-> Sai nguyên tắc trong thiết kế PM/ framework - nguyên tắc đóng gói
        log.info("User_01_Register - STEP 02: Click to radio button");
        registerPage.clickToMaleRadio();

        log.info("User_01_Register - STEP 03: Enter to Firstname textbox with value "+ UserObject.FIRST_NAME);
        registerPage.enterToFirstNameTextbox(UserObject.FIRST_NAME);

        log.info("User_01_Register - STEP 04: Enter to Lastname textbox with value "+UserObject.LAST_NAME);
        registerPage.enterToLastNameTextbox(UserObject.LAST_NAME);

        log.info("User_01_Register - STEP 05: Enter to Email textbox with value "+UserObject.EMAIL_ADDRESS);
        registerPage.enterToEmailTextbox(UserObject.EMAIL_ADDRESS);

        log.info("User_01_Register - STEP 06: Enter to Company Name textbox with value "+companyName);
        registerPage.enterToCompanyTextbox(companyName);

        log.info("User_01_Register - STEP 07: Enter to Password textbox with value "+UserObject.PASSWORD);
        registerPage.enterToPasswordTextbox(UserObject.PASSWORD);

        log.info("User_01_Register - STEP 08: Enter to Password Confirm textbox with value "+UserObject.PASSWORD);
        registerPage.enterToPasswordConfirmTextbox(UserObject.PASSWORD);

        log.info("User_01_Register - STEP 09: Click to Register button ");
        registerPage.clickRegisterButton();

        log.info("User_01_Register - STEP 10: Verify message success is displayed");
        Assert.assertEquals(registerPage.getRegisterSuccessMessage(), "Your registration completed...");
        registerPage.clickToLogoutButton();

    }

    @Test
    public void User_02_Login() {
        loginPage = registerPage.clickToLoginButton();
        loginPage.enterToEmailTextbox(UserObject.EMAIL_ADDRESS);
        loginPage.enterToPasswordTextbox(UserObject.PASSWORD);
        homePage = loginPage.clickLoginToSystem();

        //Từ loginPage qua homePage
        //Page đó đc sinh ra và làm các action
        Assert.assertTrue(homePage.isMyAccountLinkDisplayed());

    }

    @Test
    public void User_03_MyAccount() {
        customerPage = homePage.clickToMyaccountLink();
        System.out.println("from web: " + customerPage.getFirstNameTextboxValue());
        System.out.println("from variable: " + UserObject.FIRST_NAME);
        Assert.assertTrue(customerPage.isGenderMaleSelected());
        Assert.assertEquals(customerPage.getFirstNameTextboxValue(), UserObject.FIRST_NAME);

        Assert.assertEquals(customerPage.getLastNameTextboxValue(), UserObject.LAST_NAME);
//        Assert.assertEquals(customerPage.getDayDropdownSelectedValue(),"");
//        Assert.assertEquals(customerPage.getMonthDropdownSelectedValue(),"");
//        Assert.assertEquals(customerPage.getYearDropdownSelectedValue(),"");
        Assert.assertEquals(customerPage.getEmailTextboxValue(), UserObject.EMAIL_ADDRESS);
        Assert.assertEquals(customerPage.getCompanyNameTextboxValue(), companyName);
    }

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

    protected  String getEmailRandom(String prefix){
        Random ran = new Random();
        return  prefix + ran.nextInt(9999)+"@orgnature.com";

    }
    // @AfterClass
    public void afterClass() {
        driver.quit();
    }


}
