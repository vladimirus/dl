package com.ft.dl.jobs;

import static com.xeiam.sundial.SundialJobScheduler.getServletContext;
import static java.util.concurrent.TimeUnit.SECONDS;

import com.ft.apiingester.model.api.content.enriched.EnrichedContent;
import com.ft.dl.service.ArticleDownloader;
import com.ft.dl.service.ContentManager;
import com.xeiam.sundial.Job;
import com.xeiam.sundial.annotations.SimpleTrigger;
import com.xeiam.sundial.exceptions.JobInterruptException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import rx.schedulers.Schedulers;

import java.time.LocalDateTime;

@SimpleTrigger(repeatInterval = 30, timeUnit = SECONDS, repeatCount = 0)
public class GetArticlesJob extends Job {
    private final Logger LOGGER = LoggerFactory.getLogger(GetArticlesJob.class);

    @Override
    public void doRun() throws JobInterruptException {
        run(
                (ArticleDownloader) getServletContext().getAttribute("downloader"),
                (ContentManager) getServletContext().getAttribute("contentManager")
        );
    }

    private void run(ArticleDownloader downloader, ContentManager contentManager) {
        //2016-07-11T12:43:12.768 - resume from here
        LOGGER.debug("Starting to collect articles");
        downloader.download(LocalDateTime.of(2016, 8, 1, 0, 0))
                .groupBy(this::groupBy)
                .subscribe(group -> group
                        .subscribeOn(Schedulers.io())
                        .subscribe(enrichedContent -> LOGGER.debug("Written {}", contentManager.save(enrichedContent))));
    }

    private char groupBy(EnrichedContent enrichedContent) {
        try {
            return enrichedContent.getTitle().charAt(0);
        } catch (Exception e) {
            return 'a';
        }
    }
}
