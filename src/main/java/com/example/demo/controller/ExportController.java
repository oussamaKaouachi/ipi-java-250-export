package com.example.demo.controller;

import com.example.demo.entity.Client;
import com.example.demo.service.ClientService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;

/**
 * Controlleur pour réaliser les exports.
 */
@Controller
@RequestMapping("/")
public class ExportController {

    @Autowired
    private ClientService clientService;

    @GetMapping("/clients/csv")
    public void clientsCSV(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Client> clients = clientService.findAllClients();
        response.setContentType("text/csv");
        response.setHeader("Content-Disposition", "attachment; filename=\"clients.csv\"");
        PrintWriter writer = response.getWriter();
        for (Client client: clients) {
            // TODO
            writer.println(client.getNom()+";"+client.getPrenom());
        }
    }

    @GetMapping("/clients/xlsx")
    public void clientsXLSX(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Client> clients = clientService.findAllClients();
        try(Workbook workbook = new XSSFWorkbook()){
            response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
            response.setHeader("Content-Disposition", "attachment; filename=\"clients.xlsx\"");
            Sheet sheet = workbook.createSheet("Clients");

            for (Client client: clients) {
                Row headerRow = sheet.createRow((int) (client.getId()-1));
                Cell cellPrenom = headerRow.createCell(0);
                cellPrenom.setCellValue(client.getPrenom());
                Cell cellNom = headerRow.createCell(1);
                cellNom.setCellValue(client.getNom());
            }

            OutputStream out = response.getOutputStream();
            workbook.write(out);
        }
    }

}
