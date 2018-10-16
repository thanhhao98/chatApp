package com.muc;

import java.util.ArrayList;

import java.io.*;
import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.*;

public class DataClient {
    // setup path of datafile
    private final static String pathDataFile = "src/com/muc/Data.xml";
    private String username=null;
    private String password=null;
    private String id=null;

    public DataClient(String username, String password){
        this.username = username;
        this.password = password;
    }

    public DataClient(String id, String username, String password) {
        this.id = id;
        this.username = username;
        this.password = password;
    }

    private ArrayList<DataClient> getDataFromDataBase(){
        ArrayList<DataClient> ret_val = new ArrayList();
        try{
            File fXmlFile = new File(pathDataFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String id = getTagValue("id", eElement);
                    String username = getTagValue("username", eElement);
                    String pass = getTagValue("password", eElement);
                    DataClient arr_ele = new DataClient(id, username, pass);
                    ret_val.add(arr_ele);
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return ret_val;
    }

    public boolean checkUserExist(){
        if(this.checkInfo()) {
            ArrayList<DataClient> listClient = getDataFromDataBase();
            for(DataClient client: listClient){
                if(client.getUsername().equals(this.username)&&client.getPassword().equals(this.password)){
                    return true;
                }
            }
        }
        return false;
    }

    // Check info of current client
    public boolean checkInfo(){
        return true;
    }

    // Check -> add data of client to Data
    public boolean addDataClient(){
        try {
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(pathDataFile);

            Node data = doc.getFirstChild();

            Element newuser = doc.createElement("user");
            Element newid = doc.createElement("id"); newid.setTextContent(this.id);
            Element newusername = doc.createElement("username"); newusername.setTextContent(this.username);
            Element newpassword = doc.createElement("password"); newpassword.setTextContent(this.password);

            newuser.appendChild(newid); newuser.appendChild(newusername); newuser.appendChild(newpassword);
            data.appendChild(newuser);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(new File(pathDataFile));
            transformer.transform(source, result);
        }
        catch(Exception ex){
            System.out.println(ex);
            return false;
        }
        return true;
    }

    public boolean removeDataClient(){
        boolean val = false;
        try{
            File fXmlFile = new File(pathDataFile);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            doc.getDocumentElement().normalize();

            NodeList nList = doc.getElementsByTagName("user");

            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node nNode = nList.item(temp);
                if (nNode.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) nNode;
                    String username = getTagValue("username", eElement);
                    if (this.getUsername().equals(username)){
                        eElement.getParentNode().removeChild(nNode);
                        TransformerFactory transformerFactory = TransformerFactory.newInstance();
                        Transformer transformer = transformerFactory.newTransformer();
                        DOMSource source = new DOMSource(doc);
                        StreamResult result = new StreamResult(new File(pathDataFile));
                        transformer.transform(source, result);
                        val = true;
                    }
                }
            }
        }
        catch(Exception ex){
            System.out.println(ex);
        }
        return val;
    }

    public String getUsername(){
        return this.username;
    }

    public String getPassword(){
        return this.password;
    }

    public String getId(){
        return this.id;
    }

    public boolean equals(DataClient client){
        return this.username.equals(client.username);
    }


    public static String getTagValue(String sTag, Element eElement) {
        NodeList nlList = eElement.getElementsByTagName(sTag).item(0).getChildNodes();
        Node nValue = (Node) nlList.item(0);
        return nValue.getNodeValue();
    }
}
