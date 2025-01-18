package com.andrei.supermarket.repository;

import com.andrei.supermarket.model.OfferModel;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OfferRepository extends CrudRepository<OfferModel, String> {
}
