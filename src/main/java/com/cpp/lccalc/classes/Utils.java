package com.cpp.lccalc.classes;

import com.cpp.lccalc.models.CommercialOffer;
import com.cpp.lccalc.repo.CommercialOfferRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class Utils {

    @Autowired
    CommercialOfferRepository commercialOfferRepository;

    public static Long getOptimalCommercialOfferId(Set<CommercialOffer> commercialOffers){
        Long optimalBudget;
        Long optimalDuration;
        Long optimalParam;
        Long idOptimal;
        List<CommercialOffer> commercialOfferList = new ArrayList<>(commercialOffers);
        Long minBudget = commercialOfferList.get(0).getBudget();
        Long maxBudget = commercialOfferList.get(0).getBudget();
        Long minDuration = commercialOfferList.get(0).getDuration();
        Long maxDuration = commercialOfferList.get(0).getDuration();
        for (CommercialOffer co: commercialOfferList) {
            if (minBudget > co.getBudget()) minBudget = co.getBudget();
            if (maxBudget < co.getBudget()) maxBudget = co.getBudget();
            if (minDuration > co.getDuration()) minDuration = co.getDuration();
            if (maxDuration < co.getDuration()) maxDuration = co.getDuration();
        }

        idOptimal = commercialOfferList.get(0).getCoId();

        if (Objects.equals(maxBudget, minBudget)) optimalBudget = (long)0;
        else optimalBudget = (long)(0.5*(commercialOfferList.get(0).getBudget()-minBudget)/(maxBudget-minBudget));

        if (Objects.equals(maxDuration, minDuration)) optimalDuration = (long)0;
        else optimalDuration = (long)(0.5*(commercialOfferList.get(0).getDuration()-minDuration)/(maxDuration-minDuration));

        optimalDuration = (long)(0.5*(commercialOfferList.get(0).getDuration()-minDuration)/(maxDuration-minDuration));
        optimalParam = optimalBudget + optimalDuration;

        long optimalBudgetCo;
        long optimalDurationCo;
        for (CommercialOffer co: commercialOfferList) {
            if (Objects.equals(maxBudget, minBudget)) optimalBudgetCo = (long)0;
            else optimalBudgetCo = (long)(0.5*(co.getBudget()-minBudget)/(maxBudget-minBudget));

            if (Objects.equals(maxDuration, minDuration)) optimalDurationCo = (long)0;
            else optimalDurationCo = (long)(0.5*(co.getDuration()-minDuration)/(maxDuration-minDuration));

            if (optimalParam > (optimalBudgetCo + optimalDurationCo)) {
                optimalParam = optimalBudgetCo + optimalDurationCo;
                idOptimal = co.getCoId();
            }
        }

        return idOptimal;



    }
}
