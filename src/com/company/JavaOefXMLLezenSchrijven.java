package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Scanner;


public class JavaOefXMLLezenSchrijven {

    public static void main(String[] args) throws ParserConfigurationException, IOException, SAXException, TransformerException {

        Scanner scanner = new Scanner(System.in);

        System.out.print("Geef bestandsnaam: ");
        String bestandsnaam = scanner.nextLine();

        if (!bestandsnaam.endsWith(".xml"))
            bestandsnaam = bestandsnaam + ".xml";


        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
        dbf.setIgnoringElementContentWhitespace(true);
        DocumentBuilder db = dbf.newDocumentBuilder();

        Document doc = getXMLDocument(db, bestandsnaam);
        doc.normalizeDocument();
        toonChildren(doc);

        System.out.print("Geef voornaam: ");
        String voornaam = scanner.nextLine();

        System.out.print("Geef achternaam: ");
        String achternaam = scanner.nextLine();

        System.out.print("Geef geboortejaar: ");
        int geboortejaar = Integer.parseInt(scanner.nextLine());

        System.out.print("Geef geboortemaand: ");
        int geboortemaand = Integer.parseInt(scanner.nextLine());

        System.out.print("Geef geboortedag: ");
        int geboortedag = Integer.parseInt(scanner.nextLine());

        LocalDate geboortedatum = LocalDate.of(geboortejaar, geboortemaand, geboortedag);


        voegPersoonToe(doc, voornaam, achternaam, geboortedatum);

        toonChildren(doc);

        bewaarBestand(doc, bestandsnaam);
    }

    private static void voegPersoonToe(Document doc, String voornaam, String achternaam, LocalDate geboortedatum) throws TransformerException {


        Element personenElement = doc.getDocumentElement();
        Element persoonElement = doc.createElement("persoon");

        Element voornaamElement = doc.createElement("voornaam");
        voornaamElement.setTextContent(voornaam);

        Element achternaamElement = doc.createElement("achternaam");
        achternaamElement.setTextContent(achternaam);

        Element geboortedatumElement = doc.createElement("geboortedatum");
        geboortedatumElement.setTextContent(geboortedatum.toString());

        persoonElement.appendChild(voornaamElement);
        persoonElement.appendChild(achternaamElement);
        persoonElement.appendChild(geboortedatumElement);
        personenElement.appendChild(persoonElement);
    }


    private static void bewaarBestand(Document doc, String bestandsnaam) throws TransformerException {
        TransformerFactory tff = TransformerFactory.newDefaultInstance();
        DOMSource source = new DOMSource(doc);
        Transformer transformer = tff.newTransformer();
        StreamResult result = new StreamResult(bestandsnaam);
        transformer.transform(source, result);
    }

    private static void toonChildren(Document doc) {


        Element rootElement = doc.getDocumentElement();
        for (int i = 0; i < rootElement.getChildNodes().getLength(); i++) {
            if (rootElement.getChildNodes().item(i).getNodeType() == Node.ELEMENT_NODE) {

                Element persoonElement = (Element) rootElement.getChildNodes().item(i);
                Element voornaamElement = (Element) persoonElement.getElementsByTagName("voornaam").item(0);
                Element achternaamElement = (Element) persoonElement.getElementsByTagName("achternaam").item(0);
                Element geboorteDatumElement = (Element) persoonElement.getElementsByTagName("geboortedatum").item(0);

                Persoon persoon = new Persoon(voornaamElement.getTextContent(), achternaamElement.getTextContent(), LocalDate.parse(geboorteDatumElement.getTextContent()));

                System.out.printf("%s %s, geboren op %s (%d jaar oud).%n", persoon.getVoornaam(), persoon.getAchternaam(),
                        persoon.getGeboortedatum(), persoon.getLeeftijd());
            }
        }
    }

    private static Document getXMLDocument(DocumentBuilder db, String bestandsnaam) throws IOException, SAXException {
        File file = new File(bestandsnaam);
        Document doc;

        if (file.exists()) {
            doc = db.parse(file);
        } else {

            System.out.println("Bestand bestaat nog niet. Het wordt gecreëerd.");
            doc = db.newDocument();
            doc.appendChild(doc.createElement("personen"));
        }
        return doc;
    }
}


class Persoon {

    private String voornaam, achternaam;
    private LocalDate geboortedatum;


    public Persoon(String voornaam, String achternaam, LocalDate geboortedatum) {

        this.voornaam = voornaam;
        this.achternaam = achternaam;
        this.geboortedatum = geboortedatum;

    }


    public String getVoornaam() {
        return voornaam;
    }

    public String getAchternaam() {
        return achternaam;
    }

    public String getGeboortedatum() {
        return geboortedatum.toString();
    }


    public int getLeeftijd() {

        LocalDate vandaag = LocalDate.now();

        int leeftijd = vandaag.getYear() - this.geboortedatum.getYear();

        if (this.geboortedatum.getDayOfYear() > vandaag.getDayOfYear()){
            leeftijd --;
        }
        return leeftijd;
    }
}