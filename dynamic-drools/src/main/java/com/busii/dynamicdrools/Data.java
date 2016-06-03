/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.busii.dynamicdrools;

/**
 *
 * @author gavin
 */
public class Data {
    
    public Data(Object jsonBlob) {
        
    }

    /**
     * Extract data by specifying a path.
     * 
     * {
     *      max_age: 65
     *      card_fee : {
     *          gold : 1000
     *          silver: 500
     *      }
     *      age_penalty: 0(18)1(25)2(45)3
     *      cards: {
     *          gold: {
     *              rewards: 0(100)1(1000)2
     *              cap: 10000
     *          }
     *          silver: {
     *              rewards: 0(500)2(2000)2
     *              cap: 5000
     *          }
     *      }
     * }
     * 
     * get("max_age")
     * get("card_fee.gold")
     * get("card_free.$card_type")
     * get("cards.$card_type.cap")
     * get("cards.$card_type.rewards[$spend]
     * @param constantName
     * @return 
     */
    public Object get(String constantName) {
        return null;
    }
    
}
