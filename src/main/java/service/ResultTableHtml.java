package service;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class ResultTableHtml {
    private String content;
    private int tableRows;
    private List firstTestResults;
    private List secondTestResult;
    private List thirdTestResult;

    public ResultTableHtml(int rows, List firstTestResults, List secondTestResult, List thirdTestResult) {
        this.content = "";
        this.tableRows = rows;
        this.firstTestResults = firstTestResults;
        this.secondTestResult = secondTestResult;
        this.thirdTestResult = thirdTestResult;

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
                "    <th>Execute thousand queries(ms)</th>\n" +
                "    <th>DB size(kb)</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Sqlite</td>\n" +
                "    <td><a href=\"sqlite1\" target=\"_blank\">" + firstTestResults.get(0) + "</a></td>\n" +
                "    <td><a href=\"s1queries\" target=\"_blank\">" + firstTestResults.get(1) + "</a></td>\n" +
                "    <td>" + firstTestResults.get(2) + "</td\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Postgres</td>\n" +
                "    <td><a href=\"postgres1\" target=\"_blank\">" + firstTestResults.get(3) + "</a></td>\n" +
                "    <td><a href=\"p1queries\" target=\"_blank\">" + firstTestResults.get(4) + "</a></td>\n" +
                "    <td>" + firstTestResults.get(5) + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Mongo</td>\n" +
                "    <td><a href=\"mongo1\" target=\"_blank\">" + firstTestResults.get(6) + "</a></td>\n" +
                "    <td><a href=\"m1queries\" target=\"_blank\">" + firstTestResults.get(7) + "</a></td>\n" +
                "    <td>" + firstTestResults.get(8) + "</td>\n" +
                "  </tr>\n" +
                "</table>");
        sb.append("<p><b>Second test: adding enhanced indexing before inserting:</b></p>");
        sb.append("<table>\n" +
                "  <tr>\n" +
                "    <th>DataBase</th>\n" +
                "    <th>Insert " + tableRows + " tuples(ms)</th>\n" +
                "    <th>Execute thousand queries(ms)</th>\n" +
                "    <th>DB size(kb)</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Sqlite</td>\n" +
                "    <td>" + secondTestResult.get(0) + "</td>\n" +
                "    <td>" + secondTestResult.get(1) + "</td>\n" +
                "    <td>" + secondTestResult.get(2) + "</td\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Postgres</td>\n" +
                "    <td>" + secondTestResult.get(3) + "</td>\n" +
                "    <td>" + secondTestResult.get(4) + "</td>\n" +
                "    <td>" + secondTestResult.get(5) + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Mongo</td>\n" +
                "    <td>" + secondTestResult.get(6) + "</td>\n" +
                "    <td>" + secondTestResult.get(7) + "</td>\n" +
                "    <td>" + secondTestResult.get(8) + "</td>\n" +
                "  </tr>\n" +
                "</table>");
        sb.append("<p><b>Third test: adding enhanced indexing before executing queries:</b></p>");
        sb.append("<table>\n" +
                "  <tr>\n" +
                "    <th>DataBase</th>\n" +
                "    <th>Insert " + tableRows + " tuples(ms)</th>\n" +
                "    <th>Execute thousand queries(ms)</th>\n" +
                "    <th>DB size(kb)</th>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Sqlite</td>\n" +
                "    <td>" + thirdTestResult.get(0) + "</td>\n" +
                "    <td>" + thirdTestResult.get(1) + "</td>\n" +
                "    <td>" + thirdTestResult.get(2) + "</td\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Postgres</td>\n" +
                "    <td>" + thirdTestResult.get(3) + "</td>\n" +
                "    <td>" + thirdTestResult.get(4) + "</td>\n" +
                "    <td>" + thirdTestResult.get(5) + "</td>\n" +
                "  </tr>\n" +
                "  <tr>\n" +
                "    <td>Mongo</td>\n" +
                "    <td>" + thirdTestResult.get(6) + "</td>\n" +
                "    <td>" + thirdTestResult.get(7) + "</td>\n" +
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
