package ru.gb.shopservice.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import java.util.Arrays;

@Aspect
public class TrackUserActionAspect {

    @AfterReturning(value = "@annotation(TrackUserAction)", returning = "returnedValue")
    public void log(Object returnedValue) {
        System.out.println("Метод вернул значение " + returnedValue);
    }


    @Around("execution(* ru.gb.shopservice.service.rest.ShopService.*(..))")
    public Object log(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        Object returnedByMethod = joinPoint.proceed();
        long endTime = System.currentTimeMillis();
        System.out.println("Время выполнения метода " + methodName + " составило " + (endTime - startTime) + " мс");
        return returnedByMethod;
    }
}
