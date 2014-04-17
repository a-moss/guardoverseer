package com.mydeblob.guard;



import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

public class Updater
{
  private String newVersion;
  private String link;
  private boolean update;

  public Updater(String v)
    throws SAXException, IOException, ParserConfigurationException
  {
    String oldVersion = v.substring(0, 5);
    HttpURLConnection connection = (HttpURLConnection)new URL("http://dev.bukkit.org/bukkit-plugins/guardoverseer/files.rss").openConnection();
    connection.setConnectTimeout(10000);
    connection.setReadTimeout(10000);
    connection.setUseCaches(false);
    Document feed = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(connection.getInputStream());
    this.newVersion = feed.getElementsByTagName("title").item(1).getTextContent().substring(1);
    String link = feed.getElementsByTagName("link").item(1).getTextContent();
    this.link = new BufferedReader(new InputStreamReader(new URL("http://is.gd/create.php?format=simple&url=" + link).openStream())).readLine();
    if ((v.contains("SNAPSHOT")) && (!this.newVersion.equals(oldVersion))) {
      this.update = false;
      return;
    }
    this.update = (!this.newVersion.equals(oldVersion));
  }

  public boolean getUpdate()
  {
    return this.update;
  }

  public String getNewVersion() {
    return this.newVersion;
  }

  public String getLink() {
    return this.link;
  }
}