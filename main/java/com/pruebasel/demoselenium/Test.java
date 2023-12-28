package com.pruebasel.demoselenium;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.logging.Logger;

public class Test implements Runnable{
    private static final String AMBIENTE = "https://qa.sigep.gob.bo/";
    private WebDriver driver;
    private Usuario usuario;
    private String perfil;
    private String mes;
    private String anio;

    public Test(Usuario usuario, String perfil, WebDriver webDriver) {
        this.usuario = usuario;
        this.perfil = perfil;
        ChromeOptions options = new ChromeOptions();
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});
        //driver = new ChromeDriver(options);
        this.driver = webDriver;
        this.driver.manage().deleteAllCookies();
        this.driver.get(AMBIENTE);
        //this.driver.manage().window().maximize();
    }

    public void primerModal() {
        this.driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(1));
        this.driver.findElement(By.id("d2::ok")).click();
    }

    public void iniciaSesion() {
        this.driver.findElement(By.name("itUsuario")).sendKeys(this.usuario.getUsuario());
        this.driver.findElement(By.name("itContrasenya")).sendKeys(this.usuario.getPassword());
        this.driver.findElement(By.name("inputText1")).sendKeys(".");
        this.configuracionCaptcha();
        this.driver.findElement(By.id("cbLogIn")).click();
    }

    public void configuracionCaptcha() {
        try {
            Cookie ck = new Cookie("SIGEP_DEV_NO_CAPTCHA", "true");
            System.out.println(ck.toString());
            Cookie cookie = this.driver.manage().getCookieNamed("SIGEP_DEV_NO_CAPTCHA");
            System.out.println(cookie.toString());
            System.out.println(this.driver.manage().getCookies().toString());
            if (cookie == null) {
                this.driver.manage().addCookie(ck);
                System.out.println(this.driver.manage().getCookies().toString());
            }
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).info("Ocurrió un error: " + ex);
        }
    }

    public void cambiarPerfil() {
        try {
            WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
            this.driver.findElement(By.id("pt1:MENU51")).click();
            this.driver.findElement(By.id("pt1:menu151003")).click();
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("pt1:it2")));
            this.driver.findElement(By.name("pt1:it2")).sendKeys(this.perfil);
            this.driver.findElement(By.name("pt1:it2")).sendKeys(Keys.ENTER);
            Thread.sleep(1000);
            this.driver.findElement(By.xpath("//a[@id='pt1:per1:0:pt_cil1x']")).click();
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).info("Ocurrió un error: " + ex);
        }
    }

    public void menuPlanillaGestionActual() {
        driver.findElement(By.id("pt1:MENU130")).click();
        driver.findElement(By.id("pt1:MENU2607")).click();
        driver.findElement(By.id("pt1:MENU260703")).click();
        driver.findElement(By.id("pt1:menu26070301")).click();
    }

    public void crearNuevaPlanilla() {
        String dd = this.ultimoDiaMes(this.mes);
        int mm = this.mesNumero(this.mes);
        String fecha = dd + "/" + mm + "/" + this.anio;
        WebDriverWait wait = new WebDriverWait(this.driver, Duration.ofSeconds(10));
        try {
            //Datos personales
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:pt_ctb_nuevo")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.name("pt1:r1:0:pt1:itGlosa")).sendKeys("Grabacion QA");
            this.driver.findElement(By.id("pt1:r1:0:pt1:dc0:soc2::content")).click();
            WebElement dropdown = this.driver.findElement(By.id("pt1:r1:0:pt1:dc0:soc2::content"));
            dropdown.findElement(By.xpath("//option[. = 'Original']")).click();

            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:0:pt1:dc0:soc1::content")).click();
            Thread.sleep(1000);
            WebElement dropdown2 = this.driver.findElement(By.id("pt1:r1:0:pt1:dc0:soc1::content"));
            dropdown2.findElement(By.xpath("//option[. = 'Mensual']")).click();

            this.driver.findElement(By.cssSelector("#pt1\\3Ar1\\3A 0\\3Apt1\\3A dc0\\3Asoc1\\3A\\3A content > option:nth-child(5)")).click();
            this.driver.findElement(By.cssSelector("#pt1\\3Ar1\\3A 0\\3Apt1\\3Aplam82fe > .xvo")).click();
            this.driver.findElement(By.id("pt1:r1:0:pt1:modalidadId::lovIconId")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:0:pt1:modalidadId_afrLovInternalTableId:0:col1")).click();
            this.driver.findElement(By.id("pt1:r1:0:pt1:modalidadId_afrLovDialogId::ok")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.name("pt1:r1:0:pt1:it3e:0:sbPP")).click();

            this.driver.findElement(By.id("pt1:r1:0:pt1:sbcfIni::content")).click();
            WebElement dropdown3 = this.driver.findElement(By.name("pt1:r1:0:pt1:sbcfIni"));
            dropdown3.findElement(By.xpath("//option[. = '"+ this.mes +"']")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.name("pt1:r1:0:pt1:it04")).sendKeys(fecha);
            this.driver.findElement(By.cssSelector("#pt1\\3Ar1\\3A 0\\3Apt1\\3AsbcfTri\\3A\\3A content > option:nth-child("+ (mm + 1) +")")).click();
            Thread.sleep(1000);

            this.driver.findElement(By.id("pt1:r1:0:pt1:sor_a:_0")).click();
            this.driver.findElement(By.id("pt1:r1:0:pt1:sor_b:_0")).click();
            this.driver.findElement(By.id("pt1:r1:0:pt1:sor_c:_0")).click();
            this.driver.findElement(By.cssSelector("#pt1\\3Ar1\\3A 0\\3Apt1\\3A cb11 .xfv")).click();
            this.driver.findElement(By.id("pt1:r1:0:pt1:sd:cbSiguiente")).click();

            //Funcionarios
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pt1:r1:1:pt1:sd:cbSiguiente")));
            this.driver.findElement(By.id("pt1:r1:1:pt1:sd:cbSiguiente")).click();

            //Conceptos
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pt1:r1:2:pt1:sd:cbSiguiente")));
            this.driver.findElement(By.id("pt1:r1:2:pt1:sd:cbSiguiente")).click();

            //Comprobantes
            Thread.sleep(2000);
            ((JavascriptExecutor)this.driver).executeScript("document.getElementsByName('pt1:r1:3:pt1:t2:0:cuentaId')[0].style.width='50%'");
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:3:pt1:t2:0:cuentaId::lovIconId")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:3:pt1:t2:0:cuentaId_afrLovInternalTableId:1:col0")).click();
            this.driver.findElement(By.id("pt1:r1:3:pt1:t2:0:cuentaId_afrLovDialogId::ok")).click();
            Thread.sleep(1500);
            ((JavascriptExecutor)this.driver).executeScript("document.getElementsByName('pt1:r1:3:pt1:t3:0:it8')[0].style.width='50%'");
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:3:pt1:t3:0:it8::lovIconId")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:3:pt1:t3:0:it8_afrLovInternalTableId:0:col0")).click();
            this.driver.findElement(By.id("pt1:r1:3:pt1:t3:0:it8_afrLovDialogId::ok")).click();
            Thread.sleep(1500);
            ((JavascriptExecutor)this.driver).executeScript("document.getElementsByName('pt1:r1:3:pt1:t3:0:it9')[0].style.width='50%'");
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:3:pt1:t3:0:it9::lovIconId")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:r1:3:pt1:t3:0:it9_afrLovInternalTableId:0:col0")).click();
            this.driver.findElement(By.id("pt1:r1:3:pt1:t3:0:it9_afrLovDialogId::ok")).click();
            this.driver.findElement(By.id("pt1:r1:3:pt1:sd:cbSiguiente")).click();

            //Pagos
            Thread.sleep(1000);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("pt1:r1:4:pt1:sd:cbSiguiente")));
            this.driver.findElement(By.id("pt1:r1:4:pt1:sd:cbSiguiente")).click();
        } catch (Exception ex) {
            Logger.getLogger(Test.class.getName()).info("Ocurrió un error: " + ex);
        }
    }

    public void eliminarPlanilla(){
        try {
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:t1:0:cilEliminar")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:cilDocBORRAR")).click();
            Thread.sleep(1000);
            this.driver.findElement(By.id("pt1:sd:cb11")).click();
            Thread.sleep(1000);
            this.driver.quit();
        } catch (Exception ex){
            Logger.getLogger(Test.class.getName()).info("Ocurrió un error: " + ex);
        }
    }

    public int mesNumero(String mes){
        return switch (mes) {
            case "Enero" -> 1;
            case "Febrero" -> 2;
            case "Marzo" -> 3;
            case "Abril" -> 4;
            case "Mayo" -> 5;
            case "Junio" -> 6;
            case "Julio" -> 7;
            case "Agosto" -> 8;
            case "Septiembre" -> 9;
            case "Octubre" -> 10;
            case "Noviembre" -> 11;
            case "Diciembre" -> 12;
            default -> 0;
        };
    }

    public String ultimoDiaMes(String mes){
        return switch (mes) {
            case "Enero" -> "31";
            case "Febrero" -> "28";
            case "Marzo" -> "31";
            case "Abril" -> "30";
            case "Mayo" -> "31";
            case "Junio" -> "30";
            case "Julio" -> "31";
            case "Agosto" -> "31";
            case "Septiembre" -> "30";
            case "Octubre" -> "31";
            case "Noviembre" -> "30";
            case "Diciembre" -> "31";
            default -> "0";
        };
    }

    @Override
    public void run() {
        try {
            long tiempoInicio = System.currentTimeMillis();
            this.primerModal();
            this.iniciaSesion();
            this.cambiarPerfil();
            //this.menuPlanillaGestionActual();
            //this.crearNuevaPlanilla();
            long tiempoFin = System.currentTimeMillis();
            System.out.println("Tiempo de ejecución: " + (tiempoFin - tiempoInicio) / 1000.0 + " segundos");
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    /*
        ACF263834400 EJMILFPA - qa
        SCH597131800 SMSEOXMP - qa
        AGT602153100 OAYQUILY - qa
    */
}