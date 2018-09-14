package com.company;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.Scanner;


public class MainOef3 {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {

        Document doc = createXMLDocument();
        Element rootElement = doc.createElement("films");
        doc.appendChild(rootElement);

        Scanner scanner= new Scanner(System.in);

        String titel;
        String bestandsnaam;
        int jaar;
        String regisseur;

        System.out.print("Geef bestandsnaam: ");
        bestandsnaam = scanner.nextLine();

        if (!bestandsnaam.endsWith(".xml")) bestandsnaam = bestandsnaam + ".xml";

        do {

            System.out.print("Geef filmtitel: ");
            titel = scanner.nextLine();

            if (!titel.equalsIgnoreCase("stop")) {

                System.out.print("Geef jaartal: ");
                jaar = Integer.parseInt(scanner.nextLine());
                System.out.print("Geef regisseur: ");
                regisseur = scanner.nextLine();

                createFilmElement(doc, titel, jaar, regisseur);
            }

        } while (!titel.equalsIgnoreCase("stop"));

        writeXMLFilm(doc, bestandsnaam);
    }


    private static void writeXMLFilm(Document doc, String bestandsnaam) throws TransformerException {
        TransformerFactory tff = TransformerFactory.newDefaultInstance();
        Transformer transformer = tff.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);
        StreamResult result = new StreamResult(bestandsnaam);
        transformer.transform(source, result);;
    }

    private static void createFilmElement(Document doc, String titel, int jaar, String regisseur) {
        Element rootElement = doc.getDocumentElement();
        Element filmElement = doc.createElement("film");
        Element titelElement = doc.createElement("titel");
        Element jaarElement = doc.createElement("jaar");
        Element regisseurElement = doc.createElement("regisseur");
        titelElement.setTextContent(titel);
        jaarElement.setTextContent(Integer.toString(jaar));
        regisseurElement.setTextContent(regisseur);
        filmElement.appendChild(titelElement);
        filmElement.appendChild(jaarElement);
        filmElement.appendChild(regisseurElement);
        rootElement.appendChild(filmElement);
    }

    private static Document createXMLDocument() throws ParserConfigurationException {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newDefaultInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.newDocument();
        doc.setXmlStandalone(true);
        return doc;
    }
}

/*
Geef bestandsnaam: films
Geef filmtitel('stop' om te stoppen): Star Trek IV
Geef jaartal: 1986
Geef regisseur: Leonard Nimoy
Geef filmtitel('stop' om te stoppen): Star Wars IV
Geef jaartal: 1977
Geef regisseur: George Lucas
Geef filmtitel('stop' om te stoppen): stop
 */