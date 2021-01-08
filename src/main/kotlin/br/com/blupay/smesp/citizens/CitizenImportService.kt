package br.com.blupay.smesp.citizens

import br.com.blupay.smesp.children.Child
import br.com.blupay.smesp.children.categories.CategoryRepository
import br.com.blupay.smesp.core.resources.citizens.models.Autoral
import br.com.blupay.smesp.core.resources.citizens.models.CitizenImport
import br.com.blupay.smesp.core.resources.citizens.models.Eja
import br.com.blupay.smesp.core.resources.citizens.models.Infant
import br.com.blupay.smesp.core.resources.citizens.models.Interdisciplinary
import br.com.blupay.smesp.core.resources.citizens.models.Literacy
import br.com.blupay.smesp.core.resources.citizens.models.MiniGrup
import br.com.blupay.smesp.core.resources.citizens.models.Nursery
import com.opencsv.bean.CsvToBeanBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.Reader
import java.time.Instant
import java.util.UUID

@Service
class CitizenImportService(
        private val citizenRepository: CitizenRepository,
        private val categoryRepository: CategoryRepository
) {
    private val logger: Logger = LoggerFactory.getLogger(CitizenImportService::class.java)

    private val CATEGORY_NURSERY = "Educação Infantil (CEI) - Berçário I e II"
    private val CATEGORY_MINI_GROUP = "Educação Infantil (CEI) - Mini Grupo I e II"
    private val CATEGORY_INFANT = "Educação Infantil (EMEI) - Infantil I e II"
    private val CATEGORY_LITERACY = "Ensino Fundamental - Ciclo de Alfabetização"
    private val CATEGORY_INTERDISCIPLINARY = "Ensino Fundamental - Ciclo Interdisciplinar"
    private val CATEGORY_AUTORAL = "Ensino Fundamental - Ciclo Autoral"
    private val CATEGORY_EJA = "Ensino Médio e Educação de Jovens e Adultos"

    fun parseToModel(file: MultipartFile) {
        val reader = BufferedReader(InputStreamReader(file.inputStream))
        val csvToBeanBuilder = csvToBean(reader)
        val citizenImports = csvToBeanBuilder.parse()
        resolve(citizenImports)
    }

    private fun resolve(citizenImports: MutableList<CitizenImport>) {
        val categories = categoryRepository.findAll()

        var count = 1
        val citizens = citizenImports.asSequence().map { citizenImport ->

            logger.info("Create citizen ${count++}")

            val children = mutableListOf<Child>()
            children.addAll(MiniGrup().resolve(citizenImport, categories.first { it.category == CATEGORY_MINI_GROUP }))
            children.addAll(Nursery().resolve(citizenImport, categories.first { it.category == CATEGORY_NURSERY }))
            children.addAll(Infant().resolve(citizenImport, categories.first { it.category == CATEGORY_INFANT }))
            children.addAll(Literacy().resolve(citizenImport, categories.first { it.category == CATEGORY_LITERACY }))
            children.addAll(
                    Interdisciplinary().resolve(
                            citizenImport,
                            categories.first { it.category == CATEGORY_INTERDISCIPLINARY })
            )
            children.addAll(Autoral().resolve(citizenImport, categories.first { it.category == CATEGORY_AUTORAL }))
            children.addAll(Eja().resolve(citizenImport, categories.first { it.category == CATEGORY_EJA }))

            val citizen = Citizen(
                    id = UUID.randomUUID(),
                    name = "Cidadao",
                    cpf = citizenImport.noNullCpf(),
                    email = citizenImport.noNullEmail(),
                    phone = citizenImport.noNullPhone()
            )
            citizen.children = children
            citizen
        }.toList()

        val uniqueCitizens = citizens.distinctBy { it.cpf }

        logger.info("Unique citizens: ${uniqueCitizens.size}")

        count = 1
        val chankedList = uniqueCitizens.chunked(10000)
        chankedList.forEach {
            logger.info("Save ${count++} of ${chankedList.size}")
            logger.info("Start at ${Instant.now()}")
            try {
                citizenRepository.saveAll(it)
            } catch (ex: Exception) {
                logger.error(ex.message)
            }
        }
    }

    private fun csvToBean(reader: Reader) = CsvToBeanBuilder<CitizenImport>(reader)
            .withType(CitizenImport::class.java)
            .withIgnoreLeadingWhiteSpace(true)
            .withSkipLines(1)
            .build()

}