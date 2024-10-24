package ru.mai.is.config;

import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ru.mai.is.dto.request.RegistrationRequest;

import lombok.extern.slf4j.Slf4j;

/**
 * Аспект для логирования запросов и ответов от RestController`ов и Service`ов
 */
@Aspect
@Component
@Slf4j
public class LoggingAspectConfig {

    // Логирование входящих запросов и ответов контроллеров
    @Around("within(@org.springframework.web.bind.annotation.RestController *)")
    public Object logRestController(ProceedingJoinPoint joinPoint) throws Throwable {
        HttpServletRequest request =
                ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String methodName = joinPoint.getSignature().getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] arguments = joinPoint.getArgs();

        // Маскировка данных
        Object[] maskedArguments = maskSensitiveData(arguments);

        log.info("Incoming request: {} {} to {}.{} with arguments: {}",
                request.getMethod(), request.getRequestURI(), className, methodName, Arrays.toString(maskedArguments));

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception in {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        }

        log.info("Response from {}.{}: {}", className, methodName, result);

        return result;
    }

    // Логирование входов в методы сервисов
    /*@Around("within(@org.springframework.stereotype.Service *)")
    public Object logServiceMethods(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getName();
        String className = joinPoint.getTarget().getClass().getSimpleName();
        Object[] arguments = joinPoint.getArgs();

        // Маскировка данных
        Object[] maskedArguments = maskSensitiveData(arguments);

        log.info("Entering into service method {}.{} with arguments: {}", className, methodName, Arrays.toString(maskedArguments));

        Object result;
        try {
            result = joinPoint.proceed();
        } catch (Throwable e) {
            log.error("Exception in {}.{}: {}", className, methodName, e.getMessage());
            throw e;
        }

        log.info("Method {}.{} returned: {}", className, methodName, result);

        return result;
    }*/

    // Метод для маскировки конфиденциальных данных
    private Object[] maskSensitiveData(Object[] arguments) {
        return Arrays.stream(arguments)
                .map(arg -> {
                    if (arg instanceof RegistrationRequest request) {
                        // Маскируем все строковые поля на 70%
                        return new RegistrationRequest(
                                request.getUsername(),
                                maskString(request.getPassword()),
                                maskString(request.getEmail()),
                                maskString(request.getPhone())
                        );
                    }
                    return arg;
                })
                .toArray();
    }

    // Метод для маскировки 70% строки по середине
    private String maskString(String input) {
        if (input == null || input.length() <= 2) {
            return input; // Не маскируем слишком короткие строки
        }

        int length = input.length();
        int maskLength = (int) (length * 0.7); // 70% длины строки
        int start = (length - maskLength) / 2; // Определяем начало маскировки
        int end = start + maskLength; // Определяем конец маскировки

        StringBuilder masked = new StringBuilder(input);
        for (int i = start; i < end; i++) {
            masked.setCharAt(i, '*'); // Заменяем символы на '*'
        }
        return masked.toString();
    }
}
