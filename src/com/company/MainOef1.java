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


public class MainOef1 {

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        Document doc = createXMLDocument();
        Element rootElement = doc.createElement("films");

        doc.appendChild(rootElement);
        Scanner scanner= new Scanner(System.in);

        System.out.print("Geef bestandsnaam: ");
        String bestandsnaam = scanner.nextLine();
        System.out.print("Geef filmtitel: ");
        String titel = scanner.nextLine();
        System.out.print("Geef jaartal: ");
        int jaar = Integer.parseInt(scanner.nextLine());
        System.out.print("Geef regisseur: ");
        String regisseur = scanner.nextLine();
        createFilmElement(doc, titel, jaar, regisseur);
        writeXMLFilm(doc, bestandsnaam);
    }


    private static String controleerBestandsnaam (String bestandsnaam) {

        int index =  bestandsnaam.lastIndexOf(".");

        if (index == -1) {
            bestandsnaam = bestandsnaam + ".xml";

        } else {
            String extensie = bestandsnaam.substring(index + 1);

            if (!extensie.equals("xml"))
                bestandsnaam = bestandsnaam + ".xml";

        }
            return bestandsnaam;
    }


    private static void writeXMLFilm(Document doc, String bestandsnaam) throws TransformerException {
        TransformerFactory tff = TransformerFactory.newDefaultInstance();
        Transformer transformer = tff.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "yes");
        DOMSource source = new DOMSource(doc);

        StreamResult result = new StreamResult(controleerBestandsnaam(bestandsnaam));
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