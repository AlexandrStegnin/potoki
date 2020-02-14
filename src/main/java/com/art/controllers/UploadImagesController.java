package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.AnnexToContracts;
import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.AnnexToContractsService;
import com.art.service.UsersAnnexToContractsService;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class UploadImagesController {

    @Resource(name = "annexToContractsService")
    private AnnexToContractsService annexToContractsService;

    @Resource(name = "usersAnnexToContractsService")
    private UsersAnnexToContractsService usersAnnexToContractsService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @RequestMapping("/annexToContract/{fileName}")
    void getFile(HttpServletResponse response, @PathVariable String fileName) throws IOException {

        String path = System.getProperty("catalina.home") + "/pdfFiles";

        if (!fileName.contains(".pdf")) {
            fileName = fileName + ".pdf";
        }
        path = path + File.separator + fileName;
        File file = new File(path);
        FileInputStream inputStream = new FileInputStream(file);

        response.setContentType("application/pdf");
        response.setContentLength((int) file.length());
        response.setHeader("Content-Disposition", "inline;filename=\"" + fileName + "\"");
        FileCopyUtils.copy(inputStream, response.getOutputStream());

    }

    @PostMapping(value = "/savePdf", produces = {"application/json;charset=UTF-8", "application/octet-stream"})
    public @ResponseBody
    GenericResponse savePdf(@RequestParam("investorId") String invId, MultipartHttpServletRequest request) {
        GenericResponse response = new GenericResponse();
        BigInteger investorId = BigInteger.valueOf(Long.parseLong(invId));
        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()) {
            multipartFiles.add(request.getFile(itr.next()));
        }
        String path = System.getProperty("catalina.home") + "/pdfFiles";
        multipartFiles.forEach(file -> {

            if (!file.isEmpty()) {
                String name = file.getOriginalFilename();
                if (name.contains(".pdf")) {
                    File f = new File(path + File.separator + name);
                    if (!f.exists()) {
                        try {
                            byte[] bytes = file.getBytes();

                            // Creating the directory to store file
                            File dir = new File(path);
                            if (!dir.exists()) {
                                dir.mkdirs();
                            }

                            AnnexToContracts annex = new AnnexToContracts();
                            annex.setAnnexName(name);

                            UsersAnnexToContracts usersAnnexToContracts = new UsersAnnexToContracts();
                            usersAnnexToContracts.setAnnex(annex);
                            usersAnnexToContracts.setUserId(investorId);
                            usersAnnexToContracts.setAnnexRead(0);
                            usersAnnexToContracts.setDateRead(null);
                            usersAnnexToContractsService.create(usersAnnexToContracts);

                            // Create the file on server
                            File serverFile = new File(dir.getAbsolutePath()
                                    + File.separator + name);
                            BufferedOutputStream stream = new BufferedOutputStream(
                                    new FileOutputStream(serverFile));
                            stream.write(bytes);
                            stream.close();
                            response.setMessage("Файл " + name + " успешно загружен.");

                        } catch (Exception e) {
                            response.setMessage("Не удалось загрузить файл " + name + " => " + e.getMessage());
                        }
                    } else {
                        response.setMessage("Файл " + name + " уже существует.");
                    }
                } else {
                    response.setMessage("Неверный формат файла. Файл должен быть в формате PDF.");
                }


            } else {
                response.setMessage("Файл не загружен, потому что он пустой.");
            }
        });

        return response;
    }

    @PostMapping(value = "/deleteAnnex", produces = "application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deletePdf(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        AnnexToContracts annexToContracts = searchSummary.getAnnexToContracts();
        if (!annexToContracts.getAnnexName().contains(".pdf")) {
            annexToContracts.setAnnexName(annexToContracts.getAnnexName() + ".pdf");
        }
        String path = System.getProperty("catalina.home") + "/pdfFiles";
        File file = new File(path + File.separator + annexToContracts.getAnnexName());
        if (file.delete()) {
            usersAnnexToContractsService.deleteByAnnex(annexToContracts);
            annexToContractsService.deleteById(annexToContracts.getId());
            response.setMessage("Файл " + annexToContracts.getAnnexName() + " успешно удалён");
        } else {
            response.setError("При удалении файла " + annexToContracts.getAnnexName() + " произошла ошибка");
        }

        return response;
    }
}
