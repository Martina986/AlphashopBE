package com.xantrix.webapp.UnitTest.ControllerTest;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xantrix.webapp.ArticoliWebServiceApplication;

import lombok.Data;
import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;


@RunWith(SpringRunner.class)
@ContextConfiguration(classes = ArticoliWebServiceApplication.class)
@SpringBootTest
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class SelectArtTest
{
	private MockMvc mockMvc;

	private String AuthServerUrl = "http://localhost:9100/auth";
	private String tokenJwT = "";
	private String UserId = "Nicola";
	private String Password = "123Stella";

	@Autowired
	private WebApplicationContext wac;

	private final ObjectMapper objectMapper = new ObjectMapper();

	@Before
	public void setup()
			throws JSONException, IOException
	{
		this.mockMvc = MockMvcBuilders.webAppContextSetup(wac).build();

		GetTokenFromAuthSrv();
	}


	private void GetTokenFromAuthSrv()
			throws JSONException, IOException
	{

		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject userJson = new JSONObject();
		userJson.put("username", UserId);
		userJson.put("password", Password);

		HttpEntity<String> request = new HttpEntity<>(userJson.toString(), headers);

		String JsonToken = restTemplate.postForObject(AuthServerUrl, request, String.class);
		JsonNode root = objectMapper.readTree(JsonToken);
		String Token = root.path("token").asText();

		this.tokenJwT = "Bearer " + Token;
	}

	String JsonData =
			"{\r\n" +
					"    \"codArt\": \"002000301\",\r\n" +
					"    \"descrizione\": \"ACQUA ULIVETO 15 LT\",\r\n" +
					"    \"um\": \"PZ  \",\r\n" +
					"    \"codStat\": \"\",\r\n" +
					"    \"pzCart\": 6,\r\n" +
					"    \"pesoNetto\": 1.5,\r\n" +
					"    \"idStatoArt\": \"1 \",\r\n" +
					"    \"dataCreaz\": \"2010-06-14\",\r\n" +
					"    \"prezzo\": 1.07,\r\n" + //<-- Aggiunto il prezzo dell'articolo
					"    \"barcode\": [\r\n" +
					"        {\r\n" +
					"            \"barcode\": \"8008490000021\",\r\n" +
					"            \"idTipoArt\": \"CP\"\r\n" +
					"        }\r\n" +
					"    ],\r\n" +
					"    \"ingredienti\": null,\r\n" +
					"    \"iva\": {\r\n" +
					"        \"idIva\": 22,\r\n" +
					"        \"descrizione\": \"IVA RIVENDITA 22%\",\r\n" +
					"        \"aliquota\": 22\r\n" +
					"    },\r\n" +
					"    \"famAssort\": {\r\n" +
					"        \"id\": 1,\r\n" +
					"        \"descrizione\": \"DROGHERIA ALIMENTARE\"\r\n" +
					"    }\r\n" +
					"}";


	@Test
	public void B_listArtByEan() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/ean/8008490000021").header("Authorization",tokenJwT)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				//articoli
				.andExpect(jsonPath("$.codArt").exists())
				.andExpect(jsonPath("$.codArt").value("002000301"))
				.andExpect(jsonPath("$.descrizione").exists())
				.andExpect(jsonPath("$.descrizione").value("ACQUA ULIVETO 15 LT"))
				.andExpect(jsonPath("$.um").exists())
				.andExpect(jsonPath("$.um").value("PZ  "))
				.andExpect(jsonPath("$.codStat").exists())
				.andExpect(jsonPath("$.codStat").value(""))
				.andExpect(jsonPath("$.pzCart").exists())
				.andExpect(jsonPath("$.pzCart").value("6"))
				.andExpect(jsonPath("$.pesoNetto").exists())
				.andExpect(jsonPath("$.pesoNetto").value("1.5"))
				.andExpect(jsonPath("$.idStatoArt").exists())
				.andExpect(jsonPath("$.idStatoArt").value("1 "))
				.andExpect(jsonPath("$.dataCreaz").exists())
				.andExpect(jsonPath("$.dataCreaz").value("2010-06-14"))
				.andExpect(jsonPath("$.prezzo").exists())
				.andExpect(jsonPath("$.prezzo").value("1.07"))
				//barcode
				.andExpect(jsonPath("$.barcode[0].barcode").exists())
				.andExpect(jsonPath("$.barcode[0].barcode").value("8008490000021"))
				.andExpect(jsonPath("$.barcode[0].idTipoArt").exists())
				.andExpect(jsonPath("$.barcode[0].idTipoArt").value("CP"))
				//famAssort
				.andExpect(jsonPath("$.famAssort.id").exists())
				.andExpect(jsonPath("$.famAssort.id").value("1"))
				.andExpect(jsonPath("$.famAssort.descrizione").exists())
				.andExpect(jsonPath("$.famAssort.descrizione").value("DROGHERIA ALIMENTARE"))
				//ingredienti
				.andExpect(jsonPath("$.ingredienti").isEmpty())
				//Iva
				.andExpect(jsonPath("$.iva.idIva").exists())
				.andExpect(jsonPath("$.iva.idIva").value("22"))
				.andExpect(jsonPath("$.iva.descrizione").exists())
				.andExpect(jsonPath("$.iva.descrizione").value("IVA RIVENDITA 22%"))
				.andExpect(jsonPath("$.iva.aliquota").exists())
				.andExpect(jsonPath("$.iva.aliquota").value("22"))

				.andDo(print());
	}

	private String Barcode = "8008490002138";

	@Test
	public void C_ErrlistArtByEan() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/ean/" + Barcode).header("Authorization", tokenJwT)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.codice").value(404))
				.andExpect(jsonPath("$.messaggio").value("Il barcode " + Barcode + " non è stato trovato!"))
				.andDo(print());
	}

	@Test
	public void D_listArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/002000301").header("Authorization", tokenJwT)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData))
				.andReturn();
	}

	private String CodArt = "002000301b";

	@Test
	public void E_ErrlistArtByCodArt() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/codice/" + CodArt).header("Authorization", tokenJwT)
						.contentType(MediaType.APPLICATION_JSON)
						.content(JsonData)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound())
				.andExpect(jsonPath("$.codice").value(404))
				.andExpect(jsonPath("$.messaggio").value("L'articolo con codice " + CodArt + " non è stato trovato!"))
				.andDo(print());
	}

	private String JsonData2 = "[" + JsonData + "]";

	@Test
	public void F_listArtByDesc() throws Exception
	{
		mockMvc.perform(MockMvcRequestBuilders.get("/api/articoli/cerca/descrizione/ACQUA ULIVETO 15 LT").header("Authorization", tokenJwT)
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(1)))
				.andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
				.andExpect(content().json(JsonData2))
				.andReturn();
	}

	@Data
	private class Token
	{
		private String token;


	}
}
