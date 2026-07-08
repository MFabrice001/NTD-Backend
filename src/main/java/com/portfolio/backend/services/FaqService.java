package com.portfolio.backend.services;

import com.portfolio.backend.models.Faq;
import com.portfolio.backend.repositories.FaqRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class FaqService {

    private final FaqRepository faqRepository;

    @Autowired
    public FaqService(FaqRepository faqRepository) {
        this.faqRepository = faqRepository;
    }

    public List<Faq> getAllFaqs() {
        return faqRepository.findAll();
    }

    public Optional<Faq> getFaqById(Long id) {
        return faqRepository.findById(id);
    }

    public Faq createFaq(Faq faq) {
        return faqRepository.save(faq);
    }

    public Faq updateFaq(Long id, Faq updatedFaq) {
        return faqRepository.findById(id).map(faq -> {
            faq.setQuestion(updatedFaq.getQuestion());
            faq.setAnswer(updatedFaq.getAnswer());
            return faqRepository.save(faq);
        }).orElseThrow(() -> new RuntimeException("FAQ not found"));
    }

    public void deleteFaq(Long id) {
        faqRepository.deleteById(id);
    }
}
