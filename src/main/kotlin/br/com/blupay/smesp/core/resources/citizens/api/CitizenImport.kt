package br.com.blupay.smesp.core.resources.citizens.api

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.multipart.MultipartFile

object CitizenImport {
    @RequestMapping("citizens")
    interface Controller {

        @PostMapping("/import")
        fun importCsv(@RequestPart("file") file: MultipartFile)
    }
}