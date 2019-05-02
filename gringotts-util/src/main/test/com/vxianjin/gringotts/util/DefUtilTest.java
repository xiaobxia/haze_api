package com.vxianjin.gringotts.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * @Author: kiro
 * @Date: 2018/7/3
 * @Description:
 */
@RunWith(JUnit4.class)
public class DefUtilTest {

    @Test
    public void convert() {
        assert DefValueUtil.def(null, -1) == -1;
        assert DefValueUtil.def(null, "a").equals("a");
        assert DefValueUtil.def(null, 2.5f) == 2.5f;
        assert DefValueUtil.def(null, 2.5d) == 2.5d;

        assert DefValueUtil.def(1, -1) == 1;
        assert DefValueUtil.def("b", "a").equals("b");
        assert DefValueUtil.def(3.0f, 2.5f) == 3.0f;
        assert DefValueUtil.def(3.0d, 2.5d) == 3.0d;
    }
}
