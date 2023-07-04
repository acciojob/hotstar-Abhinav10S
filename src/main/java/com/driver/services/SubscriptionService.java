package com.driver.services;


import com.driver.EntryDto.SubscriptionEntryDto;
import com.driver.model.Subscription;
import com.driver.model.SubscriptionType;
import com.driver.model.User;
import com.driver.repository.SubscriptionRepository;
import com.driver.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class SubscriptionService {

    @Autowired
    SubscriptionRepository subscriptionRepository;

    @Autowired
    UserRepository userRepository;

    public Integer buySubscription(SubscriptionEntryDto subscriptionEntryDto){

        //Save The subscription Object into the Db and return the total Amount that user has to pay
        Integer numberOfScreen = subscriptionEntryDto.getNoOfScreensRequired();
        Integer amount  = 0 ;

        User user = userRepository.findById(subscriptionEntryDto.getUserId()).get() ;
        Subscription subscription = new Subscription();
        subscription.setSubscriptionType(subscriptionEntryDto.getSubscriptionType());

        if(subscriptionEntryDto.getSubscriptionType().toString().equals("PRO")){
            Integer amountWithNoOfScreenAmount = 800+(250*numberOfScreen) ;
            amount = amountWithNoOfScreenAmount ;
        }
        else if (subscriptionEntryDto.getSubscriptionType().toString().equals("BASIC")){
            Integer amountWithNoOfScreenAmount = 500+(200*numberOfScreen) ;
            amount = amountWithNoOfScreenAmount ;
        }
        else{
            Integer amountWithNoScreenAmount = 1000+(350*numberOfScreen);
                   amount = amountWithNoScreenAmount ;
        }
        subscription.setUser(user);
        subscription.setTotalAmountPaid(amount);
        subscription.setNoOfScreensSubscribed(numberOfScreen);
        return amount;
    }

    public Integer upgradeSubscription(Integer userId)throws Exception{

        //If you are already at an ElITE subscription : then throw Exception ("Already the best Subscription")
        //In all other cases just try to upgrade the subscription and tell the difference of price that user has to pay
        //update the subscription in the repository
        User user = userRepository.findById(userId).get() ;
         if(user.getSubscription().getSubscriptionType().toString().equals("ELITE")){
             throw new Exception("Already the best Subscription") ;
         }
         Subscription subscription = user.getSubscription()  ;
         Integer preAmount = subscription.getTotalAmountPaid() ;
         Integer currentAmount = 0 ;
         if(subscription.getSubscriptionType().equals(SubscriptionType.BASIC)){
             subscription.setSubscriptionType(SubscriptionType.PRO);
             currentAmount = preAmount +300+(50*subscription.getNoOfScreensSubscribed()) ;

         }
         else{
             subscription.setSubscriptionType(SubscriptionType.ELITE);
             currentAmount = preAmount +200+(100*subscription.getNoOfScreensSubscribed()) ;
         }
         subscription.setTotalAmountPaid(currentAmount);
         user.setSubscription(subscription);
         subscriptionRepository.save(subscription);
        return currentAmount - preAmount;
    }

    public Integer calculateTotalRevenueOfHotstar(){

        //We need to find out total Revenue of hotstar : from all the subscriptions combined
        //Hint is to use findAll function from the SubscriptionDb
        List<Subscription> subscriptionList = new ArrayList<>() ;
        Integer totalAmount = 0 ;

        for(Subscription subscription : subscriptionList){
            totalAmount += subscription.getTotalAmountPaid() ;
        }
        return totalAmount;
    }

}
