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
        if (commercialOffers.isEmpty()) return 0L;

        double optimalBudget;
        double optimalDuration;
        double optimalParam;
        Long idOptimal;
        List<CommercialOffer> commercialOfferList = new ArrayList<>(commercialOffers);
        double minBudget = commercialOfferList.get(0).getBudget();
        double maxBudget = commercialOfferList.get(0).getBudget();
        double minDuration = commercialOfferList.get(0).getDuration();
        double maxDuration = commercialOfferList.get(0).getDuration();
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
        else optimalDuration = (0.5*(commercialOfferList.get(0).getDuration().doubleValue()-minDuration)/(maxDuration - minDuration));

        optimalDuration = (0.5*(commercialOfferList.get(0).getDuration()-minDuration)/(maxDuration - minDuration));
        optimalParam = optimalBudget + optimalDuration;

        double optimalBudgetCo;
        double optimalDurationCo;
        for (CommercialOffer co: commercialOfferList) {
            if (Objects.equals(maxBudget, minBudget)) optimalBudgetCo = 0;
            else optimalBudgetCo = (0.5*(co.getBudget()-minBudget)/(maxBudget-minBudget));

            if (Objects.equals(maxDuration, minDuration)) optimalDurationCo = (long)0;
            else optimalDurationCo = (0.5*(co.getDuration()-minDuration)/(maxDuration-minDuration));

            if (optimalParam > (optimalBudgetCo + optimalDurationCo)) {
                optimalParam = optimalBudgetCo + optimalDurationCo;
                idOptimal = co.getCoId();
            }
        }

        return idOptimal;



    }

    public static int getIndex(String subTaskIndex){
        int index = 0;
        String indexString = subTaskIndex.substring(subTaskIndex.indexOf(".")+1);
        index = Integer.parseInt(indexString);
        return index;
    }
}
