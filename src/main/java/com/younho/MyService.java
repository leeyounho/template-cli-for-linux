package com.younho;

import org.springframework.transaction.annotation.Transactional;

public class MyService {
    private final MyRepository myRepository;

    public MyService(MyRepository myRepository) {
        this.myRepository = myRepository;
    }

    @Transactional
    public void processData(long id, String param) {
        String result = myRepository.findDataById(id);

        if ("some_condition".equals(result)) {
            System.out.println("조건에 맞는 작업을 수행합니다.");
            myRepository.updateStatus(id, param);
        } else {
            System.out.println("기본 작업을 수행합니다.");
        }
    }
}
