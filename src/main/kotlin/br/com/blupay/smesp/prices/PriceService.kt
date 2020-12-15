package br.com.blupay.smesp.prices

import org.springframework.stereotype.Service

@Service
class PriceService (private val priceRepository: PriceRepository){
    fun findById(id: Int): Price?{
        return priceRepository.findById(id).get()
    }
}