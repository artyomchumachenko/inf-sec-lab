package ru.mai.is.service.algorithm;

import org.springframework.stereotype.Service;

import ru.mai.is.algorithm.stribog.StribogImpl;
import ru.mai.is.dto.request.algorithm.StribogRequest;

import lombok.RequiredArgsConstructor;

import static ru.mai.is.algorithm.stribog.util.StringToByteArray.bytesToHex;
import static ru.mai.is.algorithm.stribog.util.StringToByteArray.castStringToBytes;

@Service
@RequiredArgsConstructor
public class StribogService {

    private final StribogImpl stribog;

    public String getHash(StribogRequest stribogRequest) {
        byte[] byteArrayForHashing = castStringToBytes(stribogRequest.getText());
        byte[] resultByteArray = stribog.getHash(byteArrayForHashing, stribogRequest.getMode().isMode());

        return bytesToHex(resultByteArray);
    }
}
