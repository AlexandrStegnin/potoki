package com.art.controllers;

import com.art.func.GetPrincipalFunc;
import com.art.model.AnnexToContracts;
import com.art.model.UsersAnnexToContracts;
import com.art.model.supporting.GenericResponse;
import com.art.model.supporting.SearchSummary;
import com.art.service.AnnexToContractsService;
import com.art.service.UsersAnnexToContractsService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Controller
public class UploadImagesController {
    private static final Logger logger = LoggerFactory
            .getLogger(UploadImagesController.class);

    @Resource(name = "annexToContractsService")
    private AnnexToContractsService annexToContractsService;

    @Resource(name = "usersAnnexToContractsService")
    private UsersAnnexToContractsService usersAnnexToContractsService;

    @Resource(name = "getPrincipalFunc")
    private GetPrincipalFunc getPrincipalFunc;

    @RequestMapping("/annexToContract/{fileName}")
    void getFile(HttpServletResponse response, @PathVariable String fileName) throws IOException {

        String path = System.getProperty("catalina.home") + "/pdfFiles";

        if(!fileName.contains(".pdf")){
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

    /*
    @GetMapping(value = "/uploadPdf")
    public ModelAndView uploadImgPage(ModelMap model, HttpSession session){

        FileBucket fileModel = new FileBucket();
        model.addAttribute("fileBucket", fileModel);
        model.addAttribute("urlToAnnex", "Приложение_к_догорам_инвестирования.pdf");
        return new ModelAndView("uploadImages");
    }
    */
    /*
    @RequestMapping(value = "/uploadPdf", method = RequestMethod.POST)
    public @ResponseBody
    String uploadFileHandler(@RequestParam("file") MultipartFile file) {
        String name = file.getOriginalFilename();
        if (!file.isEmpty()) {
            try {
                byte[] bytes = file.getBytes();

                // Creating the directory to store file
                //String rootPath = System.getProperty("catalina.home");
                File dir = new File(path);
                if (!dir.exists())
                    dir.mkdirs();

                // Create the file on server
                File serverFile = new File(dir.getAbsolutePath()
                        + File.separator + name);
                BufferedOutputStream stream = new BufferedOutputStream(
                        new FileOutputStream(serverFile));
                stream.write(bytes);
                stream.close();

                logger.info("Расположение файла = " + serverFile.getAbsolutePath());

                return "Файл " + name + " успешно загружен.";
            } catch (Exception e) {
                return "Не удалось загрузить файл " + name + " => " + e.getMessage();
            }
        } else {
            return "Файл " + name
                    + " не загружен, потому что он пустой.";
        }
    }
    */

    @PostMapping(value = "/savePdf", produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse savePdf(MultipartHttpServletRequest request) {
        GenericResponse response = new GenericResponse();

        Iterator<String> itr = request.getFileNames();
        List<MultipartFile> multipartFiles = new ArrayList<>(0);
        while (itr.hasNext()){
            multipartFiles.add(request.getFile(itr.next()));
        }
        String path = System.getProperty("catalina.home") + "/pdfFiles";
        multipartFiles.forEach(file -> {

            if (!file.isEmpty()) {
                String name = file.getOriginalFilename();
                if(name.contains(".pdf")){
                    File f = new File(path + File.separator + name);
                    if (!f.exists()){
                        try {
                            byte[] bytes = file.getBytes();

                            // Creating the directory to store file
                            File dir = new File(path);
                            if (!dir.exists()){
                                dir.mkdirs();
                            }

                            AnnexToContracts annex = new AnnexToContracts();
                            annex.setAnnexName(name);

                            UsersAnnexToContracts usersAnnexToContracts = new UsersAnnexToContracts();
                            usersAnnexToContracts.setAnnex(annex);
                            usersAnnexToContracts.setUserId(getPrincipalFunc.getPrincipalId());
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
                            response.setError("Не удалось загрузить файл " + name + " => " + e.getMessage());
                        }
                    }else{
                        response.setError("Файл " + name + " уже существует.");
                    }
                }else {
                    response.setError("Неверный формат файла. Файл должен быть в формате PDF.");
                }


            } else {
                response.setError("Файл не загружен, потому что он пустой.");
            }
        });

        return response;
    }

    @PostMapping(value = "/deleteAnnex", produces="application/json;charset=UTF-8")
    public @ResponseBody
    GenericResponse deletePdf(@RequestBody SearchSummary searchSummary) {
        GenericResponse response = new GenericResponse();
        AnnexToContracts annexToContracts = searchSummary.getAnnexToContracts();
        if(!annexToContracts.getAnnexName().contains(".pdf")){
            annexToContracts.setAnnexName(annexToContracts.getAnnexName() + ".pdf");
        }
        String path = System.getProperty("catalina.home") + "/pdfFiles";
        File file = new File(path + File.separator + annexToContracts.getAnnexName());
        if (file.delete()){
            usersAnnexToContractsService.deleteByAnnex(annexToContracts);
            annexToContractsService.deleteById(annexToContracts.getId());
            response.setMessage("Файл " + annexToContracts.getAnnexName() + " успешно удалён");
        }else {
            response.setError("При удалении файла " + annexToContracts.getAnnexName() + " произошла ошибка");
        }

        return response;
    }
}
