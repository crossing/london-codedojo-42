(ns gilded-rose.core-spec
(:require [speclj.core :refer :all]
          [gilded-rose.core :refer [update-quality item]]))

(describe "Backstage passes"
          (it "quality drops to 0 after concert is over"
              (let [backstage-pass (item "Backstage passes to a TAFKAL80ETC concert" -1 50)
                    [updated] (update-quality [backstage-pass])]
                (should (= 0 (:quality updated))))))

(describe "Conjure items"
          (it "degrades twice as fast as normal items"
              (let [conjured-item (item "Conjured item" 10 10)
                    [updated] (update-quality [conjured-item])]
                (should= 8 (:quality updated))))
          (it "quality never drops below 0"
              (let [conjured-item (item "Conjured item" 10 0)
                    [updated] (update-quality [conjured-item])]
                (should= 0 (:quality updated))))
          (it "degrades twice as fast as before after sell in date"
              (let [conjured-item (item "Conjured item" -1 10)
                    [updated] (update-quality [conjured-item])]
                (should= 6 (:quality updated))))
          (it "sell-in value decreases"
              (let [conjured-item (item "Conjured item" 2 10)
                    [updated] (update-quality [conjured-item])]
                (should= 1 (:sell-in updated)))))



