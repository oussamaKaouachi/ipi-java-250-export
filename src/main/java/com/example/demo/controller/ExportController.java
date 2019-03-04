package com.example.demo.controller;

import com.example.demo.entity.Article;
import com.example.demo.entity.Client;
import com.example.demo.entity.Facture;
import com.example.demo.entity.LigneFacture;
import com.example.demo.repository.FactureRepository;
import com.example.demo.service.ClientService;
import com.example.demo.service.FactureService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFFont;
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

    @Autowired
    private FactureService factureService;

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

    @GetMapping("/factures/xlsx")
    public void facturesXlsx(HttpServletRequest request, HttpServletResponse response) throws IOException {
        List<Facture> factures = factureService.findAllFacture();
        List<Client> clients = clientService.findAllClients();

        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition", "attachment; filename=\"factures.xlsx\"");

        Workbook workbook = new XSSFWorkbook();

        for (Client client: clients) {
            Sheet sheet = workbook.createSheet(client.getNom());
            Row headerRow = sheet.createRow(1);
            Cell cellPrenom = headerRow.createCell(0);
            cellPrenom.setCellValue(client.getPrenom());
            Cell cellNom = headerRow.createCell(1);
            cellNom.setCellValue(client.getNom());
            for (Facture facture: factures) {
                if(client.getId().equals(facture.getClient().getId())) {
                    createFacture(facture, workbook);
                }
            }
        }

        workbook.write(response.getOutputStream());
        workbook.close();
    }

    private void createFacture(Facture facture, Workbook workbook){
        //Style
        Font font= workbook.createFont();
        font.setBold(true);
        font.setColor(IndexedColors.RED.getIndex());


        CellStyle cellStyleTopBot = workbook.createCellStyle();
        cellStyleTopBot.setBorderBottom(BorderStyle.DOUBLE);
        cellStyleTopBot.setBottomBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBot.setBorderTop(BorderStyle.DOUBLE);
        cellStyleTopBot.setTopBorderColor(IndexedColors.BLUE.getIndex());

        CellStyle cellStyleTopBotLeft = workbook.createCellStyle();
        cellStyleTopBotLeft.setBorderLeft(BorderStyle.DOUBLE);
        cellStyleTopBotLeft.setLeftBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBotLeft.setBorderBottom(BorderStyle.DOUBLE);
        cellStyleTopBotLeft.setBottomBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBotLeft.setBorderTop(BorderStyle.DOUBLE);
        cellStyleTopBotLeft.setTopBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBotLeft.setFont(font);

        CellStyle cellStyleTopBotRight = workbook.createCellStyle();
        cellStyleTopBotRight.setBorderRight(BorderStyle.DOUBLE);
        cellStyleTopBotRight.setRightBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBotRight.setBorderBottom(BorderStyle.DOUBLE);
        cellStyleTopBotRight.setBottomBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBotRight.setBorderTop(BorderStyle.DOUBLE);
        cellStyleTopBotRight.setTopBorderColor(IndexedColors.BLUE.getIndex());
        cellStyleTopBotRight.setFont(font);


        Sheet sheet = workbook.createSheet("Factures"+facture.getId().toString());
        Row headerRow = sheet.createRow(2);
        Cell cellHeaderArticle = headerRow.createCell(1);
        cellHeaderArticle.setCellValue("Article");
        Cell cellHeaderQt = headerRow.createCell(2);
        cellHeaderQt.setCellValue("Quantité");
        Cell cellHeaderPrixU= headerRow.createCell(3);
        cellHeaderPrixU.setCellValue("Prix U");
        Cell cellHeaderPrixT = headerRow.createCell(4);
        cellHeaderPrixT.setCellValue("Prix Total");

        Integer r=4;
        double total=0;

        for (LigneFacture ligne:facture.getLigneFactures()) {
            Row row = sheet.createRow(r++);

            Cell cell = row.createCell(1);
            cell.setCellValue(ligne.getArticle().getLibelle());

            cell = row.createCell(2);
            double qte =ligne.getQuantite();
            cell.setCellValue(qte);

            cell = row.createCell(3);
            double pu =ligne.getArticle().getPrix();
            cell.setCellValue(pu);

            cell = row.createCell(4);
            pu =ligne.getArticle().getPrix();
            cell.setCellValue(pu*qte);

            total+=pu*qte;

        }


        Row totalRow = sheet.createRow(r++);
        Cell cell = totalRow.createCell(1);
        cell.setCellValue("Total");
        cell.setCellStyle(cellStyleTopBotLeft);
        cell = totalRow.createCell(2);
        cell.setCellStyle(cellStyleTopBot);
        cell = totalRow.createCell(3);
        cell.setCellStyle(cellStyleTopBot);
        cell = totalRow.createCell(4);
        cell.setCellValue(total);
        cell.setCellStyle(cellStyleTopBotRight);

    }

}
