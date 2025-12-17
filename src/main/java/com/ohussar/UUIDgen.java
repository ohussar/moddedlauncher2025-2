package com.ohussar;

import java.util.Scanner;
import java.util.UUID;

public class UUIDgen {

    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        while(true){
            String user = scanner.nextLine();
            System.out.println(UUID.nameUUIDFromBytes(("OfflinePlayer:"+user).getBytes()));
            if(user.equals("break")){
                break;
            }
        }

    }
}
