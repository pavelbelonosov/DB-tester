package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

public class ResultTableHtml {
    private String content;
    private long tableRows;
    private long numQueries;
    private List firstTestResults;
    private List secondTestResult;
    private List thirdTestResult;
    private String clientID;

    public ResultTableHtml(long rows, long numQueries, List firstTestResults,
                           List secondTestResult, List thirdTestResult, String clientID) {
        this.content = "";
        this.tableRows = rows;
        this.numQueries = numQueries;
        this.firstTestResults = firstTestResults;
        this.secondTestResult = secondTestResult;
        this.thirdTestResult = thirdTestResult;
        this.clientID = clientID;

    }

    public String getClientID() {
        return clientID;
    }

    public void saveFile() {
        try (FileWriter fstream = new FileWriter("result.html");
             BufferedWriter out = new BufferedWriter(fstream)) {
            out.write(content);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void createContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("<!DOCTYPE html>");
        sb.append("<html lang=\"en\">");
        sb.append("<head>");
        sb.append("<title>Results</title>");
        sb.append("<style>\n" +
                "h1 {\n" +
                "  color: black;\n" +
                "  text-align: center;\n" +
                "}\n" +
                "p {\n" +
                "  font-family: verdana;\n" +
                "  font-size: 15px;\n" +
                "}\n" +
                "table {\n" +
                "  font-family: arial, sans-serif;\n" +
                "  border-collapse: collapse;\n" +
                "  width: 100%;\n" +
                "}\n" +
                "th, td {\n" +
                "   border: 1px solid #dddddd;\n" +
                "    text-align: left;\n" +
                "    padding: 8px;\n" +
                "}\n" +
                "tr:nth-child(even) {\n" +
                "  background-color: #dddddd;\n" +
                "}</style>");
        sb.append("</head>");
        sb.append("<body> <h1>DataBase comparison</h1>");
        sb.append("<p>In testing take part three DB - Sqlite, Postgres and Mongo. " +
                "Each test starts from scratch(collection dropped, table cleared, indexes dropped). " +
                "For tests we create table with " + tableRows + " tuples containing three attributes(id, name, year). " +
                "In case of noSql, there is collection with " + tableRows + " documents containing three fields(_id(Integer), name, year).</p>");
        sb.append("<p><b>First test: no enhanced indexes:</b></p>");
        sb.append("<table>\n" +
                "  <tr>\n" +
                "    <th>DataBase</th>\n" +
                "    <th>Insert " + tableRows + " tuples(ms)</th>\n" +
                "    <th>Execute " + numQueries + " queries(ms)</th>\n" +
                "    <th>DB size(kb)</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Sqlite</td>\n" +
                "    <td><a href=\"sqlite1" + clientID + "\" target=\"_blank\">" + firstTestResults.get(0) + "</a></td>\n" +
                "    <td><a href=\"s1queries" + clientID + "\" target=\"_blank\">" + firstTestResults.get(1) + "</a></td>\n" +
                "    <td>" + firstTestResults.get(2) + "</td\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Postgres</td>\n" +
                "    <td><a href=\"postgres1" + clientID + "\" target=\"_blank\">" + firstTestResults.get(3) + "</a></td>\n" +
                "    <td><a href=\"p1queries" + clientID + "\" target=\"_blank\">" + firstTestResults.get(4) + "</a></td>\n" +
                "    <td>" + firstTestResults.get(5) + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Mongo</td>\n" +
                "    <td><a href=\"mongo1" + clientID + "\" target=\"_blank\">" + firstTestResults.get(6) + "</a></td>\n" +
                "    <td><a href=\"m1queries" + clientID + "\" target=\"_blank\">" + firstTestResults.get(7) + "</a></td>\n" +
                "    <td>" + firstTestResults.get(8) + "</td>\n" +
                "  </tr>\n" +
                "</table>");
        sb.append("<p><b>Second test: adding enhanced indexing before inserting:</b></p>");
        sb.append("<table>\n" +
                "  <tr>\n" +
                "    <th>DataBase</th>\n" +
                "    <th>Insert " + tableRows + " tuples(ms)</th>\n" +
                "    <th>Execute " + numQueries + " queries(ms)</th>\n" +
                "    <th>DB size(kb)</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Sqlite</td>\n" +
                "    <td><a href=\"sqlite2" + clientID + "\" target=\"_blank\">" + secondTestResult.get(0) + "</a></td>\n" +
                "    <td><a href=\"s2queries" + clientID + "\" target=\"_blank\">" + secondTestResult.get(1) + "</a></td>\n" +
                "    <td>" + secondTestResult.get(2) + "</td\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Postgres</td>\n" +
                "    <td><a href=\"postgres2" + clientID + "\" target=\"_blank\">" + secondTestResult.get(3) + "</a></td>\n" +
                "    <td><a href=\"p2queries" + clientID + "\" target=\"_blank\">" + secondTestResult.get(4) + "</a></td>\n" +
                "    <td>" + secondTestResult.get(5) + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Mongo</td>\n" +
                "    <td><a href=\"mongo2" + clientID + "\" target=\"_blank\">" + secondTestResult.get(6) + "</a></td>\n" +
                "    <td><a href=\"m2queries" + clientID + "\" target=\"_blank\">" + secondTestResult.get(7) + "</a></td>\n" +
                "    <td>" + secondTestResult.get(8) + "</td>\n" +
                "  </tr>\n" +
                "</table>");
        sb.append("<p><b>Third test: adding enhanced indexing before executing queries:</b></p>");
        sb.append("<table>\n" +
                "  <tr>\n" +
                "    <th>DataBase</th>\n" +
                "    <th>Insert " + tableRows + " tuples(ms)</th>\n" +
                "    <th>Execute " + numQueries + " queries(ms)</th>\n" +
                "    <th>DB size(kb)</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Sqlite</td>\n" +
                "    <td><a href=\"sqlite3" + clientID + "\" target=\"_blank\">" + thirdTestResult.get(0) + "</a></td>\n" +
                "    <td><a href=\"s3queries" + clientID + "\" target=\"_blank\">" + thirdTestResult.get(1) + "</a></td>\n" +
                "    <td>" + thirdTestResult.get(2) + "</td\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Postgres</td>\n" +
                "    <td><a href=\"postgres3" + clientID + "\" target=\"_blank\">" + thirdTestResult.get(3) + "</a></td>\n" +
                "    <td><a href=\"p3queries" + clientID + "\" target=\"_blank\">" + thirdTestResult.get(4) + "</a></td>\n" +
                "    <td>" + thirdTestResult.get(5) + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Mongo</td>\n" +
                "    <td><a href=\"mongo3" + clientID + "\" target=\"_blank\">" + thirdTestResult.get(6) + "</a></td>\n" +
                "    <td><a href=\"m3queries" + clientID + "\" target=\"_blank\">" + thirdTestResult.get(7) + "</a></td>\n" +
                "    <td>" + thirdTestResult.get(8) + "</td>\n" +
                "  </tr>\n" +
                "</table>");
        sb.append("</body>");
        sb.append("</html>");
        content = sb.toString();
    }

    public String getContent() {
        return content;
    }


}
