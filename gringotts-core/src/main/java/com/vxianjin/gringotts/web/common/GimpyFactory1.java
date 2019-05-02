package com.vxianjin.gringotts.web.common;


import com.octo.captcha.CaptchaException;
import com.octo.captcha.CaptchaQuestionHelper;
import com.octo.captcha.component.image.wordtoimage.WordToImage;
import com.octo.captcha.component.word.wordgenerator.WordGenerator;
import com.octo.captcha.image.ImageCaptcha;
import com.octo.captcha.image.gimpy.Gimpy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.JedisCluster;

import javax.servlet.http.HttpServletRequest;
import java.awt.image.BufferedImage;
import java.security.SecureRandom;
import java.util.Locale;
import java.util.Random;

public class GimpyFactory1 extends com.octo.captcha.image.ImageCaptchaFactory {

    public static final int AVAIL_TIME = 60 * 60 * 24;
    public static final String BUNDLE_QUESTION_KEY = Gimpy.class.getName();
    @Autowired
    private JedisCluster jedisCluster;
    private Random myRandom = new SecureRandom();
    private WordToImage wordToImage;
    private WordGenerator wordGenerator;

    public GimpyFactory1(WordGenerator generator, WordToImage word2image) {
        if (word2image == null) {
            throw new CaptchaException("Invalid configuration" +
                    " for a GimpyFactory : WordToImage can't be null");
        }
        if (generator == null) {
            throw new CaptchaException("Invalid configuration" +
                    " for a GimpyFactory : WordGenerator can't be null");
        }
        wordToImage = word2image;
        wordGenerator = generator;

    }

    /**
     * gimpies are ImageCaptcha
     *
     * @return the image captcha with default locale
     */
    public ImageCaptcha getImageCaptcha() {
        return getImageCaptcha(Locale.getDefault());
    }

    public WordToImage getWordToImage() {
        return wordToImage;
    }

    public WordGenerator getWordGenerator() {
        return wordGenerator;
    }


    protected Integer getRandomLength() {
        Integer wordLength;
        int range = getWordToImage().getMaxAcceptedWordLength() -
                getWordToImage().getMinAcceptedWordLength();
        int randomRange = range != 0 ? myRandom.nextInt(range + 1) : 0;
        wordLength = new Integer(randomRange +
                getWordToImage().getMinAcceptedWordLength());
        return wordLength;
    }

    @Override
    public ImageCaptcha getImageCaptcha(Locale locale) {
//		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();

        //length
        Integer wordLength = getRandomLength();

        String word = getWordGenerator().getWord(wordLength, locale);

        BufferedImage image = null;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            String RCaptchaKey = request.getParameter("RCaptchaKey");
            jedisCluster.setex(RCaptchaKey, AVAIL_TIME, word.toLowerCase());

            image = getWordToImage().getImage(word);
        } catch (Throwable e) {
            throw new CaptchaException(e);
        }

        ImageCaptcha captcha = new Gimpy1(CaptchaQuestionHelper.getQuestion(locale, BUNDLE_QUESTION_KEY),
                image, word);
        return captcha;
    }

}
