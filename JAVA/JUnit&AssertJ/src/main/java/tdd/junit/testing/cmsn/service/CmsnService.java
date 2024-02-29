package tdd.junit.testing.cmsn.service;

import org.springframework.stereotype.Service;
import tdd.junit.testing.cmsn.entity.CmsnEntity;

@Service
public class CmsnService {
    public int calcCmsn(int a, int b) {
        CmsnEntity cmsnEntity = new CmsnEntity();
        return a + b;
    }
}
