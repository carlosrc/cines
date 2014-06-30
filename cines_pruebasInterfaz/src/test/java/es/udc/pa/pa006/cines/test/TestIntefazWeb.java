package es.udc.pa.pa006.cines.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.Wait;

import com.google.common.base.Function;

public class TestIntefazWeb {
	private WebDriver driver;
	private String baseUrl;
	private StringBuffer verificationErrors = new StringBuffer();

	@Before
	public void setUp() throws Exception {
		driver = new FirefoxDriver();
		baseUrl = "http://localhost:8080/cines/";
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	@Test
	public void testLogin() throws Exception {
		WebElement element;
		driver.get(baseUrl);
		driver.findElement(By.linkText("Cartelera")).click();
		driver.findElement(By.linkText("Autenticarse")).click();
		driver.findElement(By.id("loginName")).clear();
		driver.findElement(By.id("loginName")).sendKeys("");
		driver.findElement(By.id("loginName")).clear();
		driver.findElement(By.id("loginName")).sendKeys("user");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("a");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();

		element = driver.findElement(By.id("menuWelcome"));
		assertEquals("Hola User", element.getText());
	}

	@Test
	public void testBuyTicketsWithLogin() throws Exception {
		WebElement element;
		driver.get(baseUrl);
		driver.findElement(By.linkText("Seleccionar cine favorito")).click();

		// Seleccionamos como cine favorito el primero que aparece (Espacio
		// Coruña)
		WebElement select = driver.findElement(By.id("province"));
		Select dropDown = new Select(select);
		dropDown.selectByValue("1");

		// Esperamos a que se cargue el select con AJAX
		WebElement select2 = fluentWait(By
				.xpath("//div[@id='cinemaZone']/div/div/select"));
		Select dropDown2 = new Select(select2);
		dropDown2.selectByValue("1");

		// Comprobar que estamos en la página principal y que el cine favorito
		// sea Espacio Coruña
		element = driver.findElement(By.cssSelector("h2"));
		assertEquals("Espacio Coruña", element.getText());

		// Seleccionamos la primera sesión que aparece en la cartelera y
		// guardamos la película
		String movieTitle = driver.findElement(By.cssSelector("td")).getText();
		element = driver
				.findElement(By.xpath("//span[@class='sessionHour']/a"));
		String hour = element.getText();
		driver.findElement(By.xpath("//span[@class='sessionHour']/a")).click();

		// Comprobar que en la sesión se proyectará la película elegida
		element = driver.findElement(By.cssSelector("td"));
		assertEquals(movieTitle, element.getText());

		driver.findElement(By.linkText("Comprar")).click();
		driver.findElement(By.id("loginName")).clear();
		driver.findElement(By.id("loginName")).sendKeys("");
		driver.findElement(By.id("loginName")).clear();
		driver.findElement(By.id("loginName")).sendKeys("user");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("");
		driver.findElement(By.id("password")).clear();
		driver.findElement(By.id("password")).sendKeys("a");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();

		// Comprobar que la compra es para 12 años de esclavitud a las 23:30
		element = driver.findElement(By.cssSelector("h3"));
		assertTrue(element.getText().contains(hour));

		driver.findElement(By.id("tickets")).clear();
		driver.findElement(By.id("tickets")).sendKeys("2");
		driver.findElement(By.id("cardNumber")).clear();
		driver.findElement(By.id("cardNumber")).sendKeys("1111111111111111");
		driver.findElement(By.id("expirationDateMonth")).clear();
		driver.findElement(By.id("expirationDateMonth")).sendKeys("12");
		driver.findElement(By.id("expirationDateYear")).clear();
		driver.findElement(By.id("expirationDateYear")).sendKeys("2015");
		driver.findElement(By.cssSelector("input.btn.btn-primary")).click();

		// Obtenemos el identificador de compra
		element = driver.findElement(By.cssSelector("h4"));
		assertEquals("Compra creada correctamente", element.getText());
		element = driver.findElement(By.cssSelector("h3 strong"));
		String id = element.getText();

		driver.findElement(By.linkText("Ver histórico de compras")).click();

		// Comprobamos que se ha añadido la compra al histórico, comprobando la
		// hora el nombre y el identificador
		element = driver.findElement(By.cssSelector("td"));
		assertEquals(id, element.getText());
		element = driver.findElement(By
				.xpath("//div[@id='tableZone']/table/tbody/tr[2]/td[3]"));
		assertEquals(movieTitle, element.getText());
		element = driver.findElement(By
				.xpath("//div[@id='tableZone']/table/tbody/tr[2]/td[6]"));
		assertTrue(element.getText().contains(hour));
	}

	@After
	public void tearDown() throws Exception {
		driver.quit();
		String verificationErrorString = verificationErrors.toString();
		if (!"".equals(verificationErrorString)) {
			fail(verificationErrorString);
		}
	}

	public WebElement fluentWait(final By locator) {
		Wait<WebDriver> wait = new FluentWait<WebDriver>(driver)
				.withTimeout(30, TimeUnit.SECONDS)
				.pollingEvery(5, TimeUnit.SECONDS)
				.ignoring(NoSuchElementException.class);

		WebElement foo = wait.until(new Function<WebDriver, WebElement>() {
			public WebElement apply(WebDriver driver) {
				return driver.findElement(locator);
			}
		});

		return foo;
	};

}