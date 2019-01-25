##SpringBoot文件下载

![](http://spring.io/img/homepage/icon-spring-framework.svg)
<img src="http://spring.io/img/homepage/icon-spring-framework.svg" width="100" align=center />

####第1种方法

```
    @RequestMapping("download")
    public void downloadFileAction(HttpServletRequest request, HttpServletResponse response) {
​
        response.setCharacterEncoding(request.getCharacterEncoding());
        response.setContentType("application/octet-stream");
        FileInputStream fis = null;
        try {
            File file = new File("C:\\Users\\carry\\Pictures\\198109742591463b0b7396936.jpg");
            fis = new FileInputStream(file);
            response.setHeader("Content-Disposition", "attachment; filename=" + file.getName());
            IOUtils.copy(fis, response.getOutputStream());
            response.flushBuffer();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

```

####第2种方法


```
    @GetMapping("download")
    public ResponseEntity<InputStreamResource> downloadTest() throws Exception {
        File file = new File("C:\\Users\\carry\\Pictures\\zhizi.jpg");
        InputStream inputStream = new FileInputStream(file);
        InputStreamResource inputStreamResource = new InputStreamResource(inputStream);
        HttpHeaders headers = new HttpHeaders();
        headers.add("Cache-Control", "no-cache, no-store, must-revalidate");
        headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
        headers.add("Pragma", "no-cache");
        headers.add("Expires", "0");
        return ResponseEntity
                .ok()
                .headers(headers)
                .contentLength(file.length())
                .contentType(MediaType.parseMediaType("application/octet-stream"))
                .body(inputStreamResource);
    }
```
