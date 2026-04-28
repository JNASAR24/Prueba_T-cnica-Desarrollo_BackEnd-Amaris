package com.btg.funds.domain.port.in;

import com.btg.funds.domain.model.Fund;
import java.util.List;

public interface FundQueryUseCase {
    List<Fund> getFunds();
}
